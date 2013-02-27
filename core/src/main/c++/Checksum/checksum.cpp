#include "checksum.h"

/**
 * Code copied for zlib.
 * The adler32 is refactored from https://github.com/madler/zlib/blob/master/adler32.c.
 * See http://zlib.net/
 **/


namespace Javnce
{


#define BASE 65521 /* largest prime smaller than 65536 */
#define NMAX 5552
#define DO1(buf,i) {adler += (buf)[i]; sum2 += adler;}
#define DO2(buf,i) DO1(buf,i); DO1(buf,i+1);
#define DO4(buf,i) DO2(buf,i); DO2(buf,i+2);
#define DO8(buf,i) DO4(buf,i); DO4(buf,i+4);
#define DO16(buf) DO8(buf,0); DO8(buf,8);


#define MOD(a) a %= BASE
#define MOD28(a) a %= BASE
#define MOD63(a) a %= BASE


uint32_t adler32(uint8_t const data[], uint32_t size)
{
    uint32_t adler=0;
    uint32_t sum2=0;
    unsigned n;

    /* do sizegth NMAX blocks -- requires just one modulo operation */
    while (size >= NMAX) {
        size -= NMAX;
        n = NMAX / 16; /* NMAX is divisible by 16 */
        do {
            DO16(data); /* 16 sums unrolled */
            data += 16;
        } while (--n);
        MOD(adler);
        MOD(sum2);
    }

    /* do remaining bytes (less than NMAX, still just one modulo) */
    if (size) { /* avoid modulos if none remaining */
        while (size >= 16) {
            size -= 16;
            DO16(data);
            data += 16;
        }
        while (size--) {
            adler += *data++;
            sum2 += adler;
        }
        MOD(adler);
        MOD(sum2);
    }

    /* return recombined sums */
    return adler | (sum2 << 16);
}
}