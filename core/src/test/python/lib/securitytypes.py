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

class SecurityTypes(collections.namedtuple("SecurityTypes", "number_of_security_types security_types reason_length reason_string")):
    __slots__ = ()

    def __new__(self, *arg):
        if 1 == len(arg):
            return super(self, SecurityTypes).__new__(self, len(arg[0]), arg[0], 0, '')
        if 2 == len(arg):
            return super(self, SecurityTypes).__new__(self, arg[0], '', len(arg[1]), arg[1])
        return super(self, SecurityTypes).__new__(self, *arg)

    def pack(self):
        if 0 < self.number_of_security_types:
            formatStr = ">B%dB" % (self.number_of_security_types)
            return struct.pack(formatStr, self.number_of_security_types, *self.security_types);
            
        formatStr = ">BI%ds" % (self.reason_length)
        return struct.pack(formatStr, self.number_of_security_types, self.reason_length, self.reason_string);


    @staticmethod
    def unpack(data):
        if '\0' != data[0]:
            formatStr = ">BB*"
            fields = messageutils.unpack(formatStr, data) + (0, '')
            return SecurityTypes(fields[1:])

        formatStr = ">BIs*"
        fields = messageutils.unpack(formatStr, data)
        return SecurityTypes(fields[0], '', fields[1], *fields[2:])

if __name__ == '__main__':

    msg = SecurityTypes([0x22, 0x33, 0x44])
    print
    print  msg
    print
    data = msg.pack();
    print  data.encode("hex")
    msg = SecurityTypes.unpack(data)
    print
    print  msg

    msg = SecurityTypes(0, "What ever")
    print
    print  msg
    print
    data = msg.pack();
    
    print  data.encode("hex")
    msg = SecurityTypes.unpack(data)
    print
    print  msg

