#ifndef _JNI_DEF_H_
#define _JNI_DEF_H_

#define far
#define near

#ifndef FALSE
#define FALSE               0
#endif

#ifndef TRUE
#define TRUE                1
#endif

#ifndef IN
#define IN
#endif

#ifndef OUT
#define OUT
#endif

#ifndef __out
#define __out
#endif

#define FAR                 far
#define NEAR                near
#undef FAR
#undef  NEAR
#define FAR                 far
#define NEAR                near
#ifndef CONST
#define CONST               const
#endif

#ifndef VOID
#define VOID void
typedef char CHAR;
typedef short SHORT;
typedef long LONG;
#if !defined(MIDL_PASS)
typedef int INT;
#endif
#endif


typedef unsigned char byte;
typedef byte cs_byte;
typedef unsigned char boolean;

typedef unsigned long       DWORD;
typedef int                 BOOL;
typedef unsigned char       BYTE;
typedef unsigned short      WORD;
typedef float               FLOAT;
typedef FLOAT               *PFLOAT;
typedef BOOL near           *PBOOL;
typedef BOOL far            *LPBOOL;
typedef BYTE near           *PBYTE;
typedef BYTE far            *LPBYTE;
typedef int near            *PINT;
typedef int far             *LPINT;
typedef WORD near           *PWORD;
typedef WORD far            *LPWORD;
typedef long far            *LPLONG;
typedef DWORD near          *PDWORD;
typedef DWORD far           *LPDWORD;
typedef void far            *LPVOID;
typedef CONST void far      *LPCVOID;

typedef int                 INT;
typedef unsigned int        UINT;
typedef unsigned int        *PUINT;
typedef DWORD   COLORREF;
typedef DWORD   *LPCOLORREF;



typedef signed char			INT8;
typedef signed char *		PINT8;
typedef unsigned char		UINT8;
typedef unsigned char *		PUINT8;
typedef signed short		INT16;
typedef signed short *		PINT16;
typedef unsigned short		UINT16;
typedef unsigned short *	PUINT16;
typedef signed int			INT32;
typedef signed int *		PINT32;
typedef unsigned int		UINT32;
typedef unsigned int *		PUINT32;







//typedef char CHAR;
//typedef CHAR char;
//typedef char char;

#ifndef BASETYPES
#define BASETYPES
typedef unsigned long ULONG;
typedef ULONG *PULONG;
typedef unsigned short USHORT;
typedef USHORT *PUSHORT;
typedef unsigned char UCHAR;
typedef UCHAR *PUCHAR;
typedef char *PSZ;
#endif  /* !BASETYPES */

#define HFILE_ERROR ((HFILE)-1)


typedef CHAR *PCHAR, *LPCH, *PCH;
typedef CONST CHAR *LPCCH, *PCCH;

// typedef __nullterminated CHAR *NPSTR, *LPSTR, *PSTR;
// typedef __nullterminated PSTR *PZPSTR;
// typedef __nullterminated CONST PSTR *PCZPSTR;
// typedef __nullterminated CONST CHAR *LPCSTR, *PCSTR;
// typedef __nullterminated PCSTR *PZPCSTR;
typedef CHAR *NPSTR, *LPSTR, *PSTR;
typedef CONST CHAR *LPCSTR, *PCSTR;

//typedef CHAR *NWPSTR, *LPSTR, *PWSTR;
//typedef CHAR UNALIGNED *LPUWSTR, *PUWSTR;
//typedef CONST CHAR *LPCSTR, *PCWSTR;
//typedef CONST CHAR UNALIGNED *LPCUWSTR, *PCUWSTR;

//typedef LPWCH LPTCH, PTCH;
//typedef LPSTR PTSTR, LPTSTR;
//typedef LPCSTR PCTSTR, LPCSTR;
//typedef LPUWSTR PUTSTR, LPUTSTR;
//typedef LPCUWSTR PCUTSTR, LPCUTSTR;
//typedef LPSTR LP;

typedef int INT_PTR, *PINT_PTR;
typedef unsigned int UINT_PTR, *PUINT_PTR;

typedef long LONG_PTR, *PLONG_PTR;
typedef unsigned long ULONG_PTR, *PULONG_PTR;

typedef unsigned short UHALF_PTR, *PUHALF_PTR;
typedef short HALF_PTR, *PHALF_PTR;
typedef ULONG_PTR DWORD_PTR, *PDWORD_PTR;

typedef UINT_PTR            WPARAM;
typedef LONG_PTR            LPARAM;
typedef LONG_PTR            LRESULT;
typedef long HRESULT;

typedef struct tagRECT
{
	LONG    left;
	LONG    top;
	LONG    right;
	LONG    bottom;
} RECT, *PRECT, NEAR *NPRECT, FAR *LPRECT;

typedef const RECT FAR* LPCRECT;

typedef struct _RECTL       /* rcl */
{
	LONG    left;
	LONG    top;
	LONG    right;
	LONG    bottom;
} RECTL, *PRECTL, *LPRECTL;

typedef const RECTL FAR* LPCRECTL;

typedef struct tagPOINT
{
	LONG  x;
	LONG  y;
} POINT, *PPOINT, NEAR *NPPOINT, FAR *LPPOINT;

typedef struct _POINTL      /* ptl  */
{
	LONG  x;
	LONG  y;
} POINTL, *PPOINTL;

typedef struct tagSIZE
{
	LONG        cx;
	LONG        cy;
} SIZE, *PSIZE, *LPSIZE;


#define RtlZeroMemory(Destination,Length) memset((Destination),0,(Length))

#define ZeroMemory RtlZeroMemory
//extern "C" {
//extern size_t            strlen(const char *);
//extern wint_t            towupper(wint_t);

//}
#define _tcslen strlen
//#define wlen	strlen

/*
 * DrawText() Format Flags
 */
#ifndef _DRAW_TEXT_
#define _DRAW_TEXT_
#define DT_TOP                      0x00000000
#define DT_LEFT                     0x00000000
#define DT_CENTER                   0x00000001
#define DT_RIGHT                    0x00000002
#define DT_VCENTER                  0x00000004
#define DT_BOTTOM                   0x00000008
#define DT_WORDBREAK                0x00000010
#define DT_SINGLELINE               0x00000020
#define DT_EXPANDTABS               0x00000040
#define DT_TABSTOP                  0x00000080
#define DT_NOCLIP                   0x00000100
#define DT_EXTERNALLEADING          0x00000200
#define DT_CALCRECT                 0x00000400
#define DT_NOPREFIX                 0x00000800
#define DT_INTERNAL                 0x00001000
#define DT_EDITCONTROL              0x00002000
#define DT_PATH_ELLIPSIS            0x00004000
#define DT_END_ELLIPSIS             0x00008000
#define DT_MODIFYSTRING             0x00010000
#define DT_RTLREADING               0x00020000
#define DT_WORD_ELLIPSIS            0x00040000
#define DT_NOFULLWIDTHCHARBREAK     0x00080000
#define DT_HIDEPREFIX               0x00100000
#define DT_PREFIXONLY               0x00200000
#endif //_DRAW_TEXT_

#ifndef WINAPI
#define WINAPI
#endif

typedef void *HANDLE;

typedef DWORD (WINAPI *PTHREAD_START_ROUTINE)(
	LPVOID lpThreadParameter
	);
typedef PTHREAD_START_ROUTINE LPTHREAD_START_ROUTINE;

#define MAX_PATH          260

#define RGB(r,g,b)          ((COLORREF)(((BYTE)(r)|((WORD)((BYTE)(g))<<8))|(((DWORD)(BYTE)(b))<<16)))


#ifndef max
#define max(a,b)            (((a) > (b)) ? (a) : (b))
#endif
#ifndef min
#define min(a,b)			(((a) > (b)) ? (b) : (a))
#endif
//#ifndef _T
//#define __(x)       L ## x
//#define (x)       L ## x
//#define (x)       __(x)
//#endif

#define MAKEWORD(a, b)      ((WORD)(((BYTE)(((DWORD_PTR)(a)) & 0xff)) | ((WORD)((BYTE)(((DWORD_PTR)(b)) & 0xff))) << 8))
#define MAKELONG(a, b)      ((LONG)(((WORD)(((DWORD_PTR)(a)) & 0xffff)) | ((DWORD)((WORD)(((DWORD_PTR)(b)) & 0xffff))) << 16))
#define LOWORD(l)           ((WORD)(((DWORD_PTR)(l)) & 0xffff))
#define HIWORD(l)           ((WORD)((((DWORD_PTR)(l)) >> 16) & 0xffff))
#define LOBYTE(w)           ((BYTE)(((DWORD_PTR)(w)) & 0xff))
#define HIBYTE(w)           ((BYTE)((((DWORD_PTR)(w)) >> 8) & 0xff))

#define CP_ACP 0
#define Sleep		usleep			//usleep才和windows里的Sleep是一样的
#define PI			3.1415926

#define SAFE_DELETEGLOBALREF(a) if (a != NULL) {jEnv->DeleteGlobalRef(a); a = NULL;} 


//int swprintf_x(char* _out , size_t count , const char *buf , ...);
//#define swprintf swprintf_x


static size_t strnicmp (
   char *First,
   char *Second,
   size_t Len
    )
{
    char* f = new char[Len], *s = new char[Len];


    for(int i=0;i<Len;i++)
    	f[i] = toupper(First[i]);

    for(int i=0;i<Len;i++)
    	s[i] = toupper(Second[i]);

// 返回差值，如果相等，结果为0
    int iRet = strncmp(f,s,Len);
    delete[] f;
    delete[] s;
    return iRet;
}
#endif
