/*
 * Copyright (C) 2012  Pauli Kauppinen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
#ifndef LOGGER_H
#define LOGGER_H

#include <stdio.h>
#ifndef __PRETTY_FUNCTION__
#define __PRETTY_FUNCTION__ __FUNCTION__
#endif

/**
 * Some helper macros.
 */
#define LOG_TRACE()                 printf("TRACE: %s\n", __PRETTY_FUNCTION__);fflush(stdout)
#define LOG_DEBUG(format, ...)      printf("DEBUG %s : ", __PRETTY_FUNCTION__);printf(format, ##__VA_ARGS__);printf("\n");fflush(stdout)
#define LOG_WARNING(format, ...)    fprintf(stderr, "WARNING %s : ", __PRETTY_FUNCTION__);fprintf(stderr, format, ##__VA_ARGS__);fprintf(stderr,"\n");fflush(stderr)
#define LOG_ERROR(format, ...)      fprintf(stderr, "ERROR %s : ", __PRETTY_FUNCTION__);fprintf(stderr, format, ##__VA_ARGS__);fprintf(stderr,"\n");fflush(stderr)


#endif // LOGGER_H
