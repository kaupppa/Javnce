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

class SetPixelFormat(collections.namedtuple("SetPixelFormat",
                                            "message_type  bits_per_pixel depth big_endian_flag true_colour_flag "
                                            "red_max green_max blue_max red_shift green_shift blue_shift")):
    __slots__ = ()
    _formatString = ">B3xBBBBHHHBBB3x"
    _messageType = 0


    def __new__(self, *arg):
        if 10 == len(arg):
            return super(self, SetPixelFormat).__new__(self, SetPixelFormat._messageType, *arg)
        
        return super(self, SetPixelFormat).__new__(self, *arg)
        
    def pack(self):
        return struct.pack(SetPixelFormat._formatString, *self);

    @staticmethod
    def unpack(data):
        msg = SetPixelFormat(*struct.unpack(SetPixelFormat._formatString, data))
        if msg.message_type != SetPixelFormat._messageType:
            raise Exception, 'Wrong message type'
        return msg


if __name__ == '__main__':

    msg = SetPixelFormat(1, 2, 3, 4, 6, 7, 8, 9, 10, 11)
    print 
    print  msg
    data = msg.pack();
    print
    print  data.encode("hex")
    msg = SetPixelFormat.unpack(data)
    print 
    print  msg
    print 

