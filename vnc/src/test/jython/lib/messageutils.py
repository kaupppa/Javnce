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

def unpack(formatStr, data):
    try:
        return struct.unpack(formatStr, data)
    except struct.error:
        formatlength = struct.calcsize(formatStr.replace('*', ''))
        dataLength = len(data)
        
        position = formatStr.find('*')
        format_char = formatStr[position - 1]
        count = (dataLength - formatlength) / struct.calcsize(format_char) + 1
        formatStr = ''.join((formatStr[:position - 1], str(count), format_char, formatStr[position + 1:]))
        return struct.unpack(formatStr, data)


def receiveAll(socket, count):
        data = "";
        while len(data) < count:
            data += socket.recv(count - len(data))
        return data


def receiveFBUpdate(socket, bytes_per_pixel):
    framebuffers = []
    data = socket.recv(4)
    Header = collections.namedtuple("MsgHeader", "message_type number_of_rectangles")
    header = Header(*struct.unpack(">BxH", data))

    if 0 != header.message_type:
        print  data.encode("hex")
        raise Exception, 'Wrong message type'

        
    index = 0
    while  index < header.count:
        index += 1
        data = socket.recv(12)
        FbHeader = collections.namedtuple("FBHeader", "x y width height encoding")
        fbHeader = FbHeader(*struct.unpack(">HHHHi", data))
        framebuffers.append(fbHeader)
        if 0 == fbHeader.encoding:
            data = receiveAll(socket, fbHeader.width * fbHeader.height * bytes_per_pixel)
        else:
            data = receiveAll(socket, 4)
            Header = collections.namedtuple("Header", "length")
            header = Header(*struct.unpack(">I", data))
            data = receiveAll(socket, header.length)
                
        return framebuffers

