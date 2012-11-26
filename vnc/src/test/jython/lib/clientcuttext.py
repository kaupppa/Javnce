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

class ClientCutText(collections.namedtuple("ClientCutText", "message_type length text")):
    __slots__ = ()
    _formatString = ">B3xI"
    _messageType = 6

    def __new__(self, *arg):
        if 1 == len(arg):
            return super(self, ClientCutText).__new__(self, ClientCutText._messageType, len(arg[0]), arg[0])
        return super(self, ClientCutText).__new__(self, *arg)

    def pack(self):
        formatStr = ClientCutText._formatString + "%ds" % (len(self.text))
        return struct.pack(formatStr, *self);

    @staticmethod
    def unpack(data):
        formatStr = ClientCutText._formatString + "s*"
        msg = ClientCutText(*messageutils.unpack(formatStr, data))
        if msg.message_type != ClientCutText._messageType:
            msg = None
        return msg

if __name__ == '__main__':
    msg = ClientCutText("The Client Cut Text")
    print
    print  msg
    data = msg.pack();
    print
    print  data.encode("hex")
    msg = ClientCutText.unpack(data)
    print
    print  msg

