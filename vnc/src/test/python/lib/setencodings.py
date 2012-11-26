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

class SetEncodings(collections.namedtuple("SetEncodings", "message_type number_of_encodings encodings")):
    __slots__ = ()
    _formatString = ">BxH"
    _messageType = 2
    def __new__(self, encodings):
        return super(self, SetEncodings).__new__(self, SetEncodings._messageType, len(encodings), encodings)

    def pack(self):
        formatStr = SetEncodings._formatString + "%di" % (len(self.encodings))
        return struct.pack(formatStr, self.message_type, self.number_of_encodings, *self.encodings);


    @staticmethod
    def unpack(data):
        formatStr = SetEncodings._formatString + "i*"
        fields = messageutils.unpack(formatStr, data)
        if fields[0] != SetEncodings._messageType:
            raise Exception, 'Wrong message type'
        if fields[1] != len(fields[2:]):
            raise Exception, 'Field count does not match'
        return SetEncodings(fields[2:])

if __name__ == '__main__':

    msg = SetEncodings([0, 1, 2, -223])
    print
    print  msg
    print
    data = msg.pack();
    
    print  data.encode("hex")
    msg = SetEncodings.unpack(data)
    print
    print  msg

