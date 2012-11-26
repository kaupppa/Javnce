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

class KeyEvent(collections.namedtuple("KeyEvent", "message_type  down_flag key")):
    __slots__ = ()
    _formatString = ">BB2xI"
    _messageType = 4
    
    def __new__(self, *arg):
        if 2 == len(arg):
            return super(self, KeyEvent).__new__(self, KeyEvent._messageType, *arg)
        return super(self, KeyEvent).__new__(self, *arg)

    def pack(self):
        return struct.pack(KeyEvent._formatString, *self);

    @staticmethod
    def unpack(data):
        msg = KeyEvent(*struct.unpack(KeyEvent._formatString, data))
        if msg.message_type != KeyEvent._messageType:
            raise Exception, 'Wrong message type'
        return msg

if __name__ == '__main__':

    msg = KeyEvent(False, 0x12345678)
    print
    print  msg
    
    data = msg.pack();
    print
    print  data.encode("hex")
    
    print
    msg = KeyEvent.unpack(data)
    print  msg

