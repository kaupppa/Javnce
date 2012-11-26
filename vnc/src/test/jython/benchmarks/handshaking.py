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

from lib import measurement
from lib import vncsocket
from lib import protocolversion
from lib import securitytypes
from lib import securityresult
from lib import serverinit

class Handshaking(unittest.TestCase):

    def test1(self):
        """Benchmarks RFB handshaking phase"""
        tick = measurement.Measurement("Handshake", 40)
        i = 0
        while(i < tick.count):
            i += 1
            tick.start()
            
            vncSocket = vncsocket.VncSocket()
            vncSocket.connect()
            
            # Protocol version
            msg = protocolversion.ProtocolVersion.unpack(vncSocket.receive())
            self.assertIsNotNone(msg)
            msg = protocolversion.ProtocolVersion(3, 8)
            self.assertEqual(vncSocket.send(msg.pack()), None)
        
            # Security types
            msg = securitytypes.SecurityTypes.unpack(vncSocket.receive())
            self.assertIsNotNone(msg)

            # Selected type
            self.assertEqual(vncSocket.send('\x01'), None)
        
            # SecurityResult
            msg = securityresult.SecurityResult.unpack(vncSocket.receive())
            self.assertIsNotNone(msg)
        
            # ClientInit
            self.assertEqual(vncSocket.send('\xFF'), None)
        
            # ServerInit
            msg = serverinit.ServerInit.unpack(vncSocket.receive())
            self.assertIsNotNone(msg)

            tick.stop() 

        tick.output()

def suite():

    suite = unittest.TestSuite()

    suite.addTest(unittest.makeSuite(Handshaking))

    return suite


