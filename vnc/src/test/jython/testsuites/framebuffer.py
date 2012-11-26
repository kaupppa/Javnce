# #
# Copyright (C) 2012  Pauli Kauppinen
# 
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, see <http://www.gnu.org/licenses/>.
# #
import unittest

from lib import vncsocket
from lib import protocolversion
from lib import securitytypes
from lib import securityresult
from lib import serverinit
from lib import framebufferupdaterequest
from lib import messageutils
from lib import setencodings

class FrameBuffer(unittest.TestCase):

    def tearDown(self):
        self.vncSocket = None

    def setUp(self):
        self.vncSocket = vncsocket.VncSocket()
        self.vncSocket.connect()
        
        # Protocol version
        msg = protocolversion.ProtocolVersion.unpack(self.vncSocket.receive())
        self.assertEqual(3, msg.major)
        self.assertEqual(8, msg.minor)
        msg = protocolversion.ProtocolVersion(3, 8)
        self.assertEqual(self.vncSocket.send(msg.pack()), None)
        
        # Security types
        msg = securitytypes.SecurityTypes.unpack(self.vncSocket.receive())
        self.assertTrue(0 != msg.number_of_security_types)

        # Selected type
        self.assertEqual(self.vncSocket.send('\x01'), None)
        
        # SecurityResult
        msg = securityresult.SecurityResult.unpack(self.vncSocket.receive())
        self.assertTrue(0 == msg.status)
        
        # ClientInit
        self.assertEqual(self.vncSocket.send('\xFF'), None)
        
        # ServerInit
        self.serverInit = serverinit.ServerInit.unpack(self.vncSocket.receive())
        self.assertFalse(self.serverInit.big_endian_flag)
        self.assertTrue(self.serverInit.true_colour_flag)
        self.assertTrue(self.serverInit.bits_per_pixel >= self.serverInit.depth)



    def test1(self):
        """FrameBuffers:Full RAW Native FrameBuffer"""
        
        incremental = False
        x = 0
        y = 0 
        width = self.serverInit.width
        height = self.serverInit.height
        bpp = self.serverInit.bits_per_pixel / 8
        
        req = framebufferupdaterequest.FramebufferUpdateRequest(incremental, x, y, width, height)
        

        self.assertEqual(self.vncSocket.send(req.pack()), None)
        
        fb = messageutils.receiveFBUpdate(self.vncSocket.socket, bpp) 
        self.assertEqual(len(fb), 1)
        self.assertEqual(fb[0].x, x)
        self.assertEqual(fb[0].y, y)
        self.assertEqual(fb[0].width, width)
        self.assertEqual(fb[0].height, height)
        self.assertEqual(fb[0].encoding, 0) 

    def test2(self):
        """FrameBuffers:Full RLE Native FrameBuffer"""

        encoding = -666
        # SetEncodings
        msg = setencodings.SetEncodings([encoding])
        self.assertEqual(self.vncSocket.send(msg.pack()), None)

        incremental = False
        x = 0
        y = 0 
        width = self.serverInit.width
        height = self.serverInit.height
        bpp = self.serverInit.bits_per_pixel / 8
        
        req = framebufferupdaterequest.FramebufferUpdateRequest(incremental, x, y, width, height)

        self.assertEqual(self.vncSocket.send(req.pack()), None)
            
        fb = messageutils.receiveFBUpdate(self.vncSocket.socket, bpp)
        self.assertEqual(len(fb), 1)
        self.assertEqual(fb[0].x, x)
        self.assertEqual(fb[0].y, y)
        self.assertEqual(fb[0].width, width)
        self.assertEqual(fb[0].height, height)
        self.assertEqual(fb[0].encoding, encoding) 

    def test3(self):
        """FrameBuffers:Partial RAW Native FrameBuffer"""
        
        incremental = False
        x = 30
        y = 30 
        width = self.serverInit.width - 100
        height = self.serverInit.height - 100
        bpp = self.serverInit.bits_per_pixel / 8
        
        req = framebufferupdaterequest.FramebufferUpdateRequest(incremental, x, y, width, height)
        

        self.assertEqual(self.vncSocket.send(req.pack()), None)
        
        fb = messageutils.receiveFBUpdate(self.vncSocket.socket, bpp) 
        self.assertEqual(len(fb), 1)
        self.assertEqual(fb[0].x, x)
        self.assertEqual(fb[0].y, y)
        self.assertEqual(fb[0].width, width)
        self.assertEqual(fb[0].height, height)
        self.assertEqual(fb[0].encoding, 0) 

    def test4(self):
        """FrameBuffers:Partial RLE Native FrameBuffer"""

        encoding = -666
        # SetEncodings
        msg = setencodings.SetEncodings([encoding])
        self.assertEqual(self.vncSocket.send(msg.pack()), None)

        incremental = False
        x = 30
        y = 30 
        width = self.serverInit.width - 100
        height = self.serverInit.height - 100
        bpp = self.serverInit.bits_per_pixel / 8
        
        req = framebufferupdaterequest.FramebufferUpdateRequest(incremental, x, y, width, height)

        self.assertEqual(self.vncSocket.send(req.pack()), None)
            
        fb = messageutils.receiveFBUpdate(self.vncSocket.socket, bpp)
        self.assertEqual(len(fb), 1)
        self.assertEqual(fb[0].x, x)
        self.assertEqual(fb[0].y, y)
        self.assertEqual(fb[0].width, width)
        self.assertEqual(fb[0].height, height)
        self.assertEqual(fb[0].encoding, encoding) 

    def test5(self):
        """FrameBuffers:Partial RAW Native FrameBuffer + incremental"""
        
        incremental = True
        x = 30
        y = 30 
        width = self.serverInit.width - 100
        height = self.serverInit.height - 100
        bpp = self.serverInit.bits_per_pixel / 8
        
        req = framebufferupdaterequest.FramebufferUpdateRequest(incremental, x, y, width, height)
        

        self.assertEqual(self.vncSocket.send(req.pack()), None)
        
        fb = messageutils.receiveFBUpdate(self.vncSocket.socket, bpp) 
        self.assertEqual(len(fb), 1)
        self.assertEqual(fb[0].x, x)
        self.assertEqual(fb[0].y, y)
        self.assertEqual(fb[0].width, width)
        self.assertEqual(fb[0].height, height)
        self.assertEqual(fb[0].encoding, 0) 

    def test6(self):
        """FrameBuffers:Partial RLE Native FrameBuffer + incremental"""

        encoding = -666
        # SetEncodings
        msg = setencodings.SetEncodings([encoding])
        self.assertEqual(self.vncSocket.send(msg.pack()), None)

        incremental = True
        x = 30
        y = 30 
        width = self.serverInit.width - 100
        height = self.serverInit.height - 100
        bpp = self.serverInit.bits_per_pixel / 8
        
        req = framebufferupdaterequest.FramebufferUpdateRequest(incremental, x, y, width, height)

        self.assertEqual(self.vncSocket.send(req.pack()), None)
            
        fb = messageutils.receiveFBUpdate(self.vncSocket.socket, bpp)
        self.assertEqual(len(fb), 1)
        self.assertEqual(fb[0].x, x)
        self.assertEqual(fb[0].y, y)
        self.assertEqual(fb[0].width, width)
        self.assertEqual(fb[0].height, height)
        self.assertEqual(fb[0].encoding, encoding) 

def suite():

    suite = unittest.TestSuite()

    suite.addTest(unittest.makeSuite(FrameBuffer))

    return suite


