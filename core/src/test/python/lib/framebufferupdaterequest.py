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

class FramebufferUpdateRequest(collections.namedtuple("FramebufferUpdateRequest", "message_type incremental x y width height")):
    __slots__ = ()
    _formatString = ">BBHHHH"
    _messageType = 3
    
    def __new__(self, *arg):
        if 5 == len(arg):
            return super(self, FramebufferUpdateRequest).__new__(self, FramebufferUpdateRequest._messageType, *arg)
        return super(self, FramebufferUpdateRequest).__new__(self, *arg)
    

    def pack(self):
        return struct.pack(FramebufferUpdateRequest._formatString, *self);

    @staticmethod
    def unpack(data):
        msg = FramebufferUpdateRequest(*struct.unpack(FramebufferUpdateRequest._formatString, data))
        if msg.message_type != FramebufferUpdateRequest._messageType:
            raise Exception, 'Wrong message type'
        return msg


# Testing
if __name__ == '__main__':

    msg = FramebufferUpdateRequest(False, 0, 0, 800, 480)
    print
    print  msg
    data = msg.pack();
    
    print
    print  data.encode("hex")
    
    msg = FramebufferUpdateRequest.unpack(data)
    print
    print  msg


