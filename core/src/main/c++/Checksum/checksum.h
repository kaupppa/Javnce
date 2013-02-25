#ifndef CRC32_H
#define CRC32_H

#include <stdint.h>

namespace Javnce
{
uint32_t crc32(uint8_t const data[], uint32_t size);
uint32_t adler32(uint8_t const data[], uint32_t size);
}//End of Javnce
#endif // MUTEX_H
