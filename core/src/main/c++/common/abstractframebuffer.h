#ifndef FRAMEBUFFER_H
#define FRAMEBUFFER_H

#include <stdint.h>
#include "pixelformat.h"

namespace Javnce
{

/**
 * The Class AbstractFrameBuffer defines interface for getting framebuffer.
 */
class AbstractFrameBuffer
{
public:

    virtual ~AbstractFrameBuffer(){}
    /**
     * Width getter.
     *
     * @return width of framebuffer in pixels.
     */
    virtual int getWidth() const = 0;

    /**
     * Height getter.
     *
     * @return height of framebuffer in pixels.
     */
    virtual int getHeight() const = 0;


    /**
     * Bytes per pixel getter.
     *
     * @return number of bytes per pixel.
     */
    virtual int getBytesPerPixel() const = 0;

    /**
     * Format getter.
     *
     * @return format of framebuffer.
     */
    virtual PixelFormat getFormat() const = 0;

    /**
     * Updates framebuffer i.e. graps new screen capture.
     */
    virtual void grab() = 0;

    /**
     * Framebuffer data access.
     *
     * @return pointer to first pixel.
     */
    virtual uint8_t *getData()  = 0;
};
}//End of Javnce
#endif // FRAMEBUFFER_H
