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


class Connection(unittest.TestCase):
    

    def test1(self):
        """Tests TCP connection once"""
        vncSocket = vncsocket.VncSocket()
        vncSocket.connect()
        data = vncSocket.receive(protocolversion.ProtocolVersion.getSize())
        self.assertIsNotNone(data)

    def test2(self):
        """Tests re-connecting"""
        i = 0
        while i < 10:
            i += 1
            vncSocket = vncsocket.VncSocket()
            vncSocket.connect()
            data = vncSocket.receive(protocolversion.ProtocolVersion.getSize())
            self.assertIsNotNone(data)


def suite():

    suite = unittest.TestSuite()

    suite.addTest(unittest.makeSuite(Connection))

    return suite

