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

class PointerEvent(collections.namedtuple("PointerEvent", "message_type button x y")):
    __slots__ = ()
    _formatString = ">BBHH"
    _messageType = 5
    
    def __new__(self, *arg):
        if 3 == len(arg):
            return super(self, PointerEvent).__new__(self, PointerEvent._messageType, *arg)
        return super(self, PointerEvent).__new__(self, *arg)


    def pack(self):
        return struct.pack(PointerEvent._formatString, *self);

    @staticmethod
    def unpack(data):
        msg = PointerEvent(*struct.unpack(PointerEvent._formatString, data))
        if msg.message_type != PointerEvent._messageType:
            raise Exception, 'Wrong message type'
        return msg

if __name__ == '__main__':

    msg = PointerEvent(0xff, 1024, 666)
    print
    print  msg
    
    data = msg.pack();
    print
    print  data.encode("hex")
    
    msg = PointerEvent.unpack(data)
    print
    print  msg

