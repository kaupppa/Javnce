#ifndef FRAMEBUFFER_H
#define FRAMEBUFFER_H

#include <stdint.h>
#include "pixelformat.h"

class AbstractFrameBuffer
{
public:
    virtual int getWidth() const = 0;
    virtual int getHeight() const = 0;
    virtual int getBytesPerPixel() const = 0;
    virtual PixelFormat getFormat() const = 0;
    virtual uint8_t *getData()  = 0;
};

#endif // FRAMEBUFFER_H
