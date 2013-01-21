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
import time
import sys

class Measurement:
    
    def __init__(self, name, count):
        self.name = name
        self._elapsedTime = 0.0
        self._tstart = 0
        self._counter = 0
        self.count = count
    
    def start(self):
        if self._counter == 0 : 
            sys.stdout.write(" Test progress (" + str(self.count) + " " + self.name + ") : "),
        sys.stdout.write(str(self._counter + 1))
        if self._counter < 9 : 
            sys.stdout.write(" ")
        sys.stdout.flush()

        self._tstart = time.time()

    def stop(self):
        self._counter += 1
        self._elapsedTime += time.time() - self._tstart
        sys.stdout.write("\b\b")
        sys.stdout.flush()

        
    def output(self):
        print " done."
        print "\n----------------------------------------------------------------------\n"
        print  " Measurement  : " + self.name
        print "  Test count       : %d" % self._counter
        print "  Elapsed time     : %.3f" % self._elapsedTime
        print "  Avarege time     : %.3f" % (self._elapsedTime / self._counter)
        print "  Count per second : %.3f" % (self._counter / self._elapsedTime)
        print "\n----------------------------------------------------------------------\n"


