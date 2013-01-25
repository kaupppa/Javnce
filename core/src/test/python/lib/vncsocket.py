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
import socket

class VncSocket:
    
    def __init__(self):
        self.address = "127.0.0.1"
        #self.address = "192.168.0.101"
        
        self.port = 5900
        # self.port = 5901
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)

    
    def connect(self):
        self.socket.connect((self.address, self.port))
        self.socket.settimeout(2)

    def __del__ (self):
        self.socket.shutdown(socket.SHUT_RDWR)
        self.socket.close()
        
    def send(self, data):
        # Jython workaround. Should returns None but returns number of bytes 
        self.socket.sendall(data)
        return None

    def receive(self, count=200):
        return self.socket.recv(count)
