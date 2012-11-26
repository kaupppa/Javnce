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
import struct
import collections

class ProtocolVersion(collections.namedtuple("ProtocolVersion", "major minor")):
    __slots__ = ()
    _formatString = ">4s3ss3ss"
    
    def __new__(self, *arg):
        return super(self, ProtocolVersion).__new__(self, *arg)

    def pack(self):
        return "RFB {0:03d}.{1:03d}\n".format(self.major, self.minor)

    @staticmethod
    def getSize():
        return struct.calcsize(ProtocolVersion._formatString)

    @staticmethod
    def unpack(data):
        fields = struct.unpack(ProtocolVersion._formatString, data)
        if fields[0] != "RFB ":
            raise Exception, 'Wrong message'
        if fields[2] != ".":
            raise Exception, 'Wrong message'
        if fields[4] != "\n":
            raise Exception, 'Wrong message'
        return ProtocolVersion(int(fields[1]), int(fields[3]))

if __name__ == '__main__':

    msg = ProtocolVersion(3, 8)
    print
    print  msg
    
    data = msg.pack();
    print data
    print  data.encode("hex")
    
    print
    msg = ProtocolVersion.unpack(data)
    print  msg

