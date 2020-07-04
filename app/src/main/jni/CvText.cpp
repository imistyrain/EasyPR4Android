#include <cwchar>
#include <clocale>
#include <cctype>
#include <utility>
#include "CvText.h"

CvText::CvText(const char *fontName) {
    if (fontName == nullptr) {
        std::cout << "字体名称为空" << std::endl;
        return;
    }
    // 打开字库文件, 创建一个字体
    if (FT_Init_FreeType(&m_library)) throw;
    if (FT_New_Face(m_library, fontName, 0, &m_face)) throw;
    FT_Select_Charmap(m_face, FT_ENCODING_UNICODE);
    
    // 设置字体输出参数
    restoreFont();
    // 设置C语言的字符集环境
    setlocale(LC_ALL, "");
}

// 释放FreeType资源
CvText::~CvText() {
    FT_Done_Face(m_face);
    FT_Done_FreeType(m_library);
}

// 设置字体属性
void CvText::setFont(int *type, cv::Scalar *size, bool *underline, float *diaphaneity) {
    // 参数合法性检查
    if (type) {
        if (*type >= 0) m_fontType = *type;
    }
    if (size) {
        m_fontSize.val[0] = fabs(size->val[0]);
        m_fontSize.val[1] = fabs(size->val[1]);
        m_fontSize.val[2] = fabs(size->val[2]);
        m_fontSize.val[3] = fabs(size->val[3]);
    }
    if (underline) {
        m_fontUnderline = *underline;
    }
    if (diaphaneity) {
        m_fontDiaphaneity = *diaphaneity;
    }
}

// 恢复默认的字体设置
void CvText::restoreFont() {
    m_fontType = 0;             // 字体类型(不支持)
    
    m_fontSize.val[0] = 32;     // 字体大小
    m_fontSize.val[1] = 0.8;    // 空白字符大小比例
    m_fontSize.val[2] = 0.1;    // 间隔大小比例
    m_fontSize.val[3] = 0;      // 旋转角度(不支持)
    
    m_fontUnderline = false;    // 下画线(不支持)
    
    m_fontDiaphaneity = 1.0;    // 色彩比例(可产生透明效果)
    
    // 设置字符大小
    FT_Set_Pixel_Sizes(m_face, (FT_UInt)m_fontSize.val[0], 0);
}


int CvText::putText(cv::Mat &frame, std::string text, cv::Point pos, cv::Scalar color) {
    return putText(frame, text.c_str(), pos, std::move(color));
}


#ifdef WIN32
/**
 * 将char字符数组转换为wchar_t字符数组
 * @param src char字符数组
 * @param dst wchar_t字符数组
 * @param locale 语言环境，mbstowcs函数依赖此值来判断src的编码方式
 * @return 运行成功返回0,否则返回-1
 */
int CvText::char2Wchar(const char *&src, wchar_t *&dst, const char *locale)
{
    if (src == nullptr) {
        dst = nullptr;
        return 0;
    }
    
    // 设置C语言的字符集环境
    setlocale(LC_CTYPE, locale);
    
    // 得到转化为需要的宽字符大小
    int w_size = (int)mbstowcs(nullptr, src, 0) + 1;
    
    // w_size = 0 说明mbstowcs返回值为-1。即在运行过程中遇到了非法字符(很有可能是locale没有设置正确)
    if (w_size == 0) {
        dst = nullptr;
        return -1;
    }
    
    dst = new wchar_t[w_size];
    if (dst == nullptr) {
        return -1;
    }
    
    auto ret = (int)mbstowcs(dst, src, strlen(src) + 1);
    if (ret <= 0) {
        return -1;
    }
    return ret;
}
int CvText::putText(cv::Mat &frame, const char *text, cv::Point pos, cv::Scalar color) {
    
    
    if (frame.empty())
        return -1;
    if (text == nullptr)
        return -1;
    
    wchar_t *w_str ;
    int count = char2Wchar(text, w_str);
    //
    int i=0;
    for (; i<count; ++i) {
        wchar_t wc = w_str[i];
        //如果是ascii字符(范围0~127)，调整字体大小
        //因为ascii字符在同样的m_fontSize下更小，所以要放大一点
        if(wc<128)
            FT_Set_Pixel_Sizes(m_face, (FT_UInt)(m_fontSize.val[0]*1.15), 0);
        else
            FT_Set_Pixel_Sizes(m_face, (FT_UInt)m_fontSize.val[0], 0);
        // 输出当前的字符
        putWChar(frame, wc, pos, color);
    }
    delete(w_str);
    return i;
}
#else
std::string wchar_to_UTF8(const wchar_t * in)
{
    std::string out;
    unsigned int codepoint = 0;
    for (in; *in != 0; ++in)
    {
        if (*in >= 0xd800 && *in <= 0xdbff)
            codepoint = ((*in - 0xd800) << 10) + 0x10000;
        else
        {
            if (*in >= 0xdc00 && *in <= 0xdfff)
                codepoint |= *in - 0xdc00;
            else
                codepoint = *in;
            
            if (codepoint <= 0x7f)
                out.append(1, static_cast<char>(codepoint));
            else if (codepoint <= 0x7ff)
            {
                out.append(1, static_cast<char>(0xc0 | ((codepoint >> 6) & 0x1f)));
                out.append(1, static_cast<char>(0x80 | (codepoint & 0x3f)));
            }
            else if (codepoint <= 0xffff)
            {
                out.append(1, static_cast<char>(0xe0 | ((codepoint >> 12) & 0x0f)));
                out.append(1, static_cast<char>(0x80 | ((codepoint >> 6) & 0x3f)));
                out.append(1, static_cast<char>(0x80 | (codepoint & 0x3f)));
            }
            else
            {
                out.append(1, static_cast<char>(0xf0 | ((codepoint >> 18) & 0x07)));
                out.append(1, static_cast<char>(0x80 | ((codepoint >> 12) & 0x3f)));
                out.append(1, static_cast<char>(0x80 | ((codepoint >> 6) & 0x3f)));
                out.append(1, static_cast<char>(0x80 | (codepoint & 0x3f)));
            }
            codepoint = 0;
        }
    }
    return out;
}
std::wstring UTF8_to_wchar(const char * in)
{
    std::wstring out;
    unsigned int codepoint;
    while (*in != 0)
    {
        unsigned char ch = static_cast<unsigned char>(*in);
        if (ch <= 0x7f)
            codepoint = ch;
        else if (ch <= 0xbf)
            codepoint = (codepoint << 6) | (ch & 0x3f);
        else if (ch <= 0xdf)
            codepoint = ch & 0x1f;
        else if (ch <= 0xef)
            codepoint = ch & 0x0f;
        else
            codepoint = ch & 0x07;
        ++in;
        if (((*in & 0xc0) != 0x80) && (codepoint <= 0x10ffff))
        {
            if (sizeof(wchar_t) > 2)
                out.append(1, static_cast<wchar_t>(codepoint));
            else if (codepoint > 0xffff)
            {
                out.append(1, static_cast<wchar_t>(0xd800 + (codepoint >> 10)));
                out.append(1, static_cast<wchar_t>(0xdc00 + (codepoint & 0x03ff)));
            }
            else if (codepoint < 0xd800 || codepoint >= 0xe000)
                out.append(1, static_cast<wchar_t>(codepoint));
        }
    }
    return out;
}
int CvText::putText(cv::Mat &frame, const char *text, cv::Point pos, cv::Scalar color) {
    if (frame.empty())
        return -1;
    if (text == nullptr)
        return -1;
    std::wstring dest = UTF8_to_wchar(text);
    int count = dest.length();
    //
    int i = 0;
    for (; i < count; ++i) {
        wchar_t wc = dest[i];
        //如果是ascii字符(范围0~127)，调整字体大小
        //因为ascii字符在同样的m_fontSize下更小，所以要放大一点
        if (wc < 128)
            FT_Set_Pixel_Sizes(m_face, (FT_UInt)(m_fontSize.val[0] * 1.15), 0);
        else
            FT_Set_Pixel_Sizes(m_face, (FT_UInt)m_fontSize.val[0], 0);
        // 输出当前的字符
        putWChar(frame, wc, pos, color);
    }
    return i;
}
#endif
// 输出当前字符, 更新m_pos位置
void CvText::putWChar(cv::Mat &frame, wchar_t wc, cv::Point &pos, cv::Scalar color) {
    // 根据unicode生成字体的二值位图
    IplImage img = IplImage(frame);
    
    FT_UInt glyph_index = FT_Get_Char_Index(m_face, (FT_ULong)wc);
    FT_Load_Glyph(m_face, glyph_index, FT_LOAD_DEFAULT);
    FT_Render_Glyph(m_face->glyph, FT_RENDER_MODE_MONO);
    
    FT_GlyphSlot slot = m_face->glyph;
    
    // 行列数
    int rows = slot->bitmap.rows;
    int cols = slot->bitmap.width;
    
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            int off = ((img.origin == 0) ? i : (rows - 1 - i)) * slot->bitmap.pitch + j / 8;
            
            if (slot->bitmap.buffer[off] & (0xC0 >> (j % 8))) {
                int r = (img.origin == 0) ? pos.y - (rows - 1 - i) : pos.y + i;;
                int c = pos.x + j;
                
                if (r >= 0 && r < img.height
                    && c >= 0 && c < img.width) {
                    CvScalar scalar = cvGet2D(&img, r, c);
                    
                    // 进行色彩融合
                    float p = m_fontDiaphaneity;
                    for (int k = 0; k < 4; ++k) {
                        scalar.val[k] = scalar.val[k] * (1 - p) + color.val[k] * p;
                    }
                    cvSet2D(&img, r, c, scalar);
                }
            }
        } // end for
    } // end for
    
    // 修改下一个字的输出位置
    double space = m_fontSize.val[0] * m_fontSize.val[1];
    double sep = m_fontSize.val[0] * m_fontSize.val[2];
    
    pos.x += (int)((cols ? cols : space) + sep);
}

