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
import time
from lib import vncsocket
from lib import protocolversion
from lib import securitytypes
from lib import securityresult
from lib import serverinit

class Handshaking(unittest.TestCase):
    
    def test1(self):
        """RFB handshaking phase: Normal case """
        
        vncSocket = vncsocket.VncSocket()
        vncSocket.connect()
        
        # Protocol version
        msg = protocolversion.ProtocolVersion.unpack(vncSocket.receive())
        self.assertEqual(3, msg.major)
        self.assertEqual(8, msg.minor)
        msg = protocolversion.ProtocolVersion(3, 8)
        self.assertEqual(vncSocket.send(msg.pack()), None)
        
        # Security types
        msg = securitytypes.SecurityTypes.unpack(vncSocket.receive())
        self.assertTrue(0 != msg.number_of_security_types)

        # Selected type
        self.assertEqual(vncSocket.send('\x01'), None)
        
        # SecurityResult
        msg = securityresult.SecurityResult.unpack(vncSocket.receive())
        self.assertTrue(0 == msg.status)
        
        # ClientInit
        self.assertEqual(vncSocket.send('\xFF'), None)
        
        # ServerInit
        msg = serverinit.ServerInit.unpack(vncSocket.receive())
        self.assertFalse(msg.big_endian_flag)
        self.assertTrue(msg.true_colour_flag)
        self.assertTrue(msg.bits_per_pixel >= msg.depth)
        

    def test2(self):
        """RFB handshaking phase: Wrong protocol version"""
        
        vncSocket = vncsocket.VncSocket()
        vncSocket.connect()
        
        # Protocol version
        msg = protocolversion.ProtocolVersion.unpack(vncSocket.receive())
        self.assertEqual(3, msg.major)
        self.assertEqual(8, msg.minor)
        msg = protocolversion.ProtocolVersion(3, 7)
        self.assertEqual(vncSocket.send(msg.pack()), None)
        
        # Security types
        data = vncSocket.receive()
        if 0 != len(data):
            # If some data before disconnect check it
            msg = securitytypes.SecurityTypes.unpack(data)
            self.assertTrue(0 == msg.number_of_security_types)
            self.assertTrue(0 < msg.reason_length)

        # Must have disconnect
        self.assertTrue(0 == len(vncSocket.receive()))
        
    def test3(self):
        """RFB handshaking phase : Wrong security type"""
        
        vncSocket = vncsocket.VncSocket()
        vncSocket.connect()
        
        # Protocol version
        msg = protocolversion.ProtocolVersion.unpack(vncSocket.receive())
        self.assertEqual(3, msg.major)
        self.assertEqual(8, msg.minor)
        msg = protocolversion.ProtocolVersion(3, 8)
        self.assertEqual(vncSocket.send(msg.pack()), None)
        
        # Security types
        msg = securitytypes.SecurityTypes.unpack(vncSocket.receive())
        self.assertTrue(0 != msg.number_of_security_types)

        # Selected type
        self.assertEqual(vncSocket.send('\xFF'), None)
        
        # Security types
        data = vncSocket.receive()
        if 0 != len(data):
            # If some data before disconnect check it
            # SecurityResult
            msg = securityresult.SecurityResult.unpack(data)
            self.assertTrue(1 == msg.status)
            self.assertTrue(0 < msg.reason_length)

        # Must have disconnect
        self.assertTrue(0 == len(vncSocket.receive()))
        

    def test4(self):
        """RFB handshaking phase: Stupid clienting send one byte at the time"""
        
        vncSocket = vncsocket.VncSocket()
        vncSocket.connect()
        
        # Protocol version
        msg = protocolversion.ProtocolVersion.unpack(vncSocket.receive())
        self.assertEqual(3, msg.major)
        self.assertEqual(8, msg.minor)
        
        
        data = 'RFB 003.008\n\x01\xFF'
        for i in range(len(data)):
            self.assertEqual(vncSocket.send(data[i]), None)
            time.sleep(0.01)

        # Security types
        msg = securitytypes.SecurityTypes.unpack(vncSocket.receive(2))
        self.assertTrue(0 != msg.number_of_security_types)

        # SecurityResult
        msg = securityresult.SecurityResult.unpack(vncSocket.receive(4))
        self.assertTrue(0 == msg.status)
        
        # ServerInit
        msg = serverinit.ServerInit.unpack(vncSocket.receive())
        self.assertFalse(msg.big_endian_flag)
        self.assertTrue(msg.true_colour_flag)
        self.assertTrue(msg.bits_per_pixel >= msg.depth)
        
    def test5(self):
        """RFB handshaking phase: Smartass clienting send all at once"""
        
        vncSocket = vncsocket.VncSocket()
        vncSocket.connect()
        
        # Protocol version
        msg = protocolversion.ProtocolVersion.unpack(vncSocket.receive())
        self.assertEqual(3, msg.major)
        self.assertEqual(8, msg.minor)
        
        
        data = 'RFB 003.008\n\x01\xFF'
        self.assertEqual(vncSocket.send(data), None)

        # Security types
        msg = securitytypes.SecurityTypes.unpack(vncSocket.receive(2))
        self.assertTrue(0 != msg.number_of_security_types)

        # SecurityResult
        msg = securityresult.SecurityResult.unpack(vncSocket.receive(4))
        self.assertTrue(0 == msg.status)
        
        # ServerInit
        msg = serverinit.ServerInit.unpack(vncSocket.receive())
        self.assertFalse(msg.big_endian_flag)
        self.assertTrue(msg.true_colour_flag)
        self.assertTrue(msg.bits_per_pixel >= msg.depth)
        


def suite():

    suite = unittest.TestSuite()

    suite.addTest(unittest.makeSuite(Handshaking))

    return suite

