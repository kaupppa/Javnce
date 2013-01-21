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
import messageutils

class SecurityResult(collections.namedtuple("SecurityResult", "status reason_length reason_string")):
    __slots__ = ()

    def __new__(self, *arg):
        if 1 == len(arg):
            return super(self, SecurityResult).__new__(self, arg[0], 0, '')
        if 2 == len(arg):
            return super(self, SecurityResult).__new__(self, arg[0], len(arg[1]), arg[1])
        return super(self, SecurityResult).__new__(self, *arg)

    def pack(self):
        if 0 != self.reason_length:
            formatStr = ">II%ds" % (self.reason_length)
            return struct.pack(formatStr, *self);
        return struct.pack(">I", self.status);


    @staticmethod
    def unpack(data):
        formatStr = ">I"
        if 4 < len(data):
            formatStr = ">IIs*"
        return SecurityResult(*messageutils.unpack(formatStr, data))

if __name__ == '__main__':

    msg = SecurityResult(0)
    print
    print  msg
    print
    data = msg.pack();
    
    print  data.encode("hex")
    msg = SecurityResult.unpack(data)
    print
    print  msg

    msg = SecurityResult(1, "What ever")
    print
    print  msg
    print
    data = msg.pack();
    print  data.encode("hex")
    msg = SecurityResult.unpack(data)
    print
    print  msg

