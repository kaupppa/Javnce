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

class ServerInit(collections.namedtuple("ServerInit", "width height bits_per_pixel depth big_endian_flag true_colour_flag "
                                                      "red_max green_max blue_max red_shift green_shift blue_shift name_length name")):
    __slots__ = ()
    _formatString = ">HHBBBBHHHBBB3xI"
    def __new__(self, *arg):
        if 13 == len(arg):
            fields = arg[:12] + (len(arg[12]), arg[12])
            return super(self, ServerInit).__new__(self, *fields)
        return super(self, ServerInit).__new__(self, *arg)
    
    def pack(self):
        formatStr = ServerInit._formatString + "%ds" % (len(self.name))
        return struct.pack(formatStr, *self);

    @staticmethod
    def unpack(data):
        formatStr = ServerInit._formatString + "s*"
        return ServerInit(*messageutils.unpack(formatStr, data))



if __name__ == '__main__':

    msg = ServerInit(800, 400, 32, 24, 0, 1, 255, 255, 255, 16, 8, 0, "Name, a very long name              this is")
    print
    print  msg
    
    data = msg.pack();
    print
    print  data.encode("hex")
    
    msg = ServerInit.unpack(data)
    print
    print  msg


