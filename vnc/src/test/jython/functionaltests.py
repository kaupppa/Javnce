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
import testsuites.connection
import testsuites.handshaking
import testsuites.setup
import testsuites.framebuffer
import example.VncServer.VncServer

if __name__ == '__main__':
    vncServer = example.VncServer.VncServer()
    vncServer.launch()
    
    suite = unittest.TestSuite()
    suite.addTest(testsuites.connection.suite())
    suite.addTest(testsuites.handshaking.suite())
    suite.addTest(testsuites.setup.suite())
    suite.addTest(testsuites.framebuffer.suite())


    unittest.TextTestRunner(verbosity=2).run(suite)
    
    vncServer.shutdown()
    
    print " done."


