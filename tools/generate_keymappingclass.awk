#!/usr/bin/gawk -f

################################################################################
# Copyright (C) 2013  Pauli Kauppinen
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
################################################################################

################################################################################
# The script generates KeysymMapping.Java
# Usage :
# 
# sudo apt-get install gawk
# ./generate_keys.awk /usr/include/X11/keysymdef.h
################################################################################

BEGIN { 
    nonUnicodeIndex = 0
    unicodeIndex = 0
    className = "KeyMap"
    packageName ="org.javnce.vnc.common"
        
    #sections defines what ifdefs are parsed
    sections["XK_MISCELLANY"] = 1
    sections["XK_XKB_KEYS"] = 0
    sections["XK_3270"] = 0
    sections["XK_LATIN1"] = 1
    sections["XK_LATIN2"] = 1
    sections["XK_LATIN3"] = 1
    sections["XK_LATIN4"] = 1
    sections["XK_LATIN8"] = 1
    sections["XK_LATIN9"] = 1
    sections["XK_KATAKANA"] = 0
    sections["XK_ARABIC"] = 0
    sections["XK_CYRILLIC"] = 0
    sections["XK_GREEK"] = 0
    sections["XK_TECHNICAL"] = 0
    sections["XK_SPECIAL"] = 0
    sections["XK_PUBLISHING"] = 0
    sections["XK_APL"] = 0
    sections["XK_HEBREW"] = 0
    sections["XK_THAI"] = 0
    sections["XK_KOREAN"] = 0
    sections["XK_ARMENIAN"] = 0
    sections["XK_GEORGIAN"] = 0
    sections["XK_CAUCASUS"] = 0
    sections["XK_VIETNAMESE"] = 0
    sections["XK_CURRENCY"] = 0
    sections["XK_MATHEMATICAL"] = 0
    sections["XK_BRAILLE"] = 0
    sections["XK_SINHALA"] = 0

    sectionAllowed = 0
    keyIndex = 0

}
/^#ifdef/    {
                section = $2
                #print "section " $2
                sectionAllowed = 0;
                if ($2 in sections)
                {
                    sectionAllowed = sections[$2]
                }
            }
/^#endif/    {
                sectionAllowed = 0
            }

/deprecated/ { 
                #Ignore deprecated
                next 
             }

/dead/ { 
                #Ignore dead
                next 
             }
/^#define/   {
                if (!sectionAllowed)
                {
                    next
                }
                if (5 < NF && $0 ~ /U+/)
                {
                    pos = index($0, "U+")
                    if (0 != pos)
                    {
                        unicode = substr($0, pos)
                        split(unicode, array, " ")
                        unicode = array[1]
                        addUnicode($2, $3, unicode)
                        next
                    }
                }
                addNoneUnicode($2, $3)
            }
END {
    writeClassHeader()

    writeConstants()
    
    write_createUnicodeMap()

    write_unicodeToKeySym()

    write_addMapping()

    writeClassEnd()

} 

function hexTrim(hexValue)
{
    # Remove leading U, x, X, + and 0
    sub("[UxX0+]*", "", hexValue)
    return strtonum("0x" hexValue);
}

function addUnicode(keyName, keysym, unicode)
{
    unicodeIndex++;

    unicodeName[unicodeIndex] = keyName;
    unicodeKeysym[unicodeIndex] = hexTrim(keysym);
    unicodeUnicode[unicodeIndex] = hexTrim(unicode);
}

function addNoneUnicode(keyName, keysym)
{
    nonUnicodeIndex ++
    nonUnicodeName[nonUnicodeIndex] = keyName
    nonUnicodeValue[nonUnicodeIndex] = keysym
}


function writeConstants()
{
    printf("\n")
    printf("    public static final int %s = 0x%x;\n", "None", 0)
    printf("    public static final int %s = 0x%x;\n", "KeySym_base", 0x01000000)
    printf("\n")
    printf("    //Next are none unicode constants. See keysymdef.h\n")
    for (i = 1; i <= nonUnicodeIndex; i++)
    {
        printf("    public static final int %s = %s;\n", nonUnicodeName[i], nonUnicodeValue[i])
    }

    printf("\n")
    printf("    //Next are unicode constants to be mapped. See keysymdef.h\n")
    for (i = 1; i <= unicodeIndex; i++)
    {
        sym = unicodeKeysym[i]
        unicode = unicodeUnicode[i]
        if (sym != unicode && sym != (unicode + 0x01000000))
        {
            printf("    public static final int %s = 0x%x;\n", unicodeName[i], sym)
        }
    }
}


function writeClassHeader()
{
    printf("/*")
    printf(" * Copyright (C) 2013  Pauli Kauppinen\n")
    printf(" * \n")
    printf(" * This program is free software; you can redistribute it and/or\n")
    printf(" * modify it under the terms of the GNU General Public License\n")
    printf(" * as published by the Free Software Foundation; either version 2\n")
    printf(" * of the License, or (at your option) any later version.\n")
    printf(" * \n")
    printf(" * This program is distributed in the hope that it will be useful,\n")
    printf(" * but WITHOUT ANY WARRANTY; without even the implied warranty of\n")
    printf(" * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n")
    printf(" * GNU General Public License for more details.\n")
    printf(" * \n")
    printf(" * You should have received a copy of the GNU General Public License\n")
    printf(" * along with this program; if not, see <http://www.gnu.org/licenses/>.\n")
    printf(" */\n")
    printf("\n")
    printf("package %s;\n", packageName)
    printf("\n")

    printf("import java.util.HashMap;\n")
    printf("import java.util.Map;\n")
    
    printf("\n")
    printf("/**\n")
    printf(" * The key code mapping class.\n")
    printf(" *\n")
    printf(" * Generated file, do not edit manually.\n")
    printf(" * Generated with command : tools/generate_keymappingclass.awk")
    for (i = 0; i < ARGC; i++)
    {
            printf( " %s", ARGV[i])
    }
    printf("\n")
    printf(" */\n")
    
    printf("public class %s<T> {\n\n", className)
    
    printf("    /**\n")
    printf("     * The unicode to keysym map.\n")
    printf("     */\n")
    printf("    static final private Map<Integer, Integer> unicodeToKeysymMap = createUnicodeMap();\n")
    
    printf("    /**\n")
    printf("     * The custom map.\n")
    printf("     */\n")
    printf("    final private Map<T, Integer> map;\n")

    printf("\n")
    printf("    public %s() {\n", className)
    printf("        this.map = new HashMap<>();\n")
    printf("    }\n")
}

function writeClassEnd()
{
    printf "}\n"
}

function write_createUnicodeMap()
{
    printf("\n")
    printf("    static private Map<Integer, Integer> createUnicodeMap()\n")
    printf("    {\n")

    printf("        Map<Integer, Integer> map = new HashMap<>();\n")
    for (i = 1; i <= unicodeIndex; i++)
    {
        sym = unicodeKeysym[i]
        unicode = unicodeUnicode[i]
        if (sym != unicode && sym != (unicode + 0x01000000))
        {
            printf("        map.put(new Integer(0x%X), new Integer(%s));\n", unicode, unicodeName[i])
        }
    }
    printf("        return map;\n")
    printf("    }\n")
}

function write_addMapping()
{
    printf("\n")
    printf("    /**\n")
    printf("     * Adds keysym mapping.\n")
    printf("     *\n")
    printf("     * @param item is the value to be mapped\n")
    printf("     * @param keysym is the mapped value of code\n")
    printf("     */\n")
    printf("    public void addMapping(T item, int keysym)\n")
    printf("    {\n")
    printf("        map.put(item, new Integer(keysym));\n")
    printf("    }\n")
    printf("\n")
    
    printf("    /**\n")
    printf("     * Gets the mapped value.\n")
    printf("     *\n")
    printf("     * @param item is the value to be searched\n")
    printf("     *\n")
    printf("     * @return keysym value or None if not mapped\n")
    printf("     */\n")
    printf("    public int getMapped(T item) {\n")
    printf("        int keysum = None;\n")
    printf("        if (map.containsKey(item)) {\n")
    printf("            keysum = map.get(item);\n")
    printf("        }\n")
    printf("        return keysum;\n")
    printf("    }\n")
    printf("\n")

}

function write_unicodeToKeySym()
{
    printf("\n")
    printf("    /**\n")
    printf("     * converts unicode value to X11 keysym value.\n")
    printf("     *\n")
    printf("     * @param unicode is the unicode value to be converted\n")
    printf("     * \n")
    printf("     * @return keysym value or None if not a unicode \n")
    printf("     */\n")
    printf("    static public int unicodeToKeySym(int unicode)\n")
    printf("    {\n")
    printf("        int keysum = None;\n")
    printf("        \n")
    printf("        if (0x20 <= unicode && unicode <= 0x7e)\n")
    printf("        {\n")
    printf("            keysum = unicode;\n")
    printf("        }\n")
    printf("        else if (0xa0 <= unicode && unicode <= 0xff)\n")
    printf("        {\n")
    printf("            keysum = unicode;\n")
    printf("        }\n")
    printf("        else if (0x100 <= unicode && unicode <= 0x10FFFF)\n")
    printf("        {\n")
    printf("            if (unicodeToKeysymMap.containsKey(unicode))\n")
    printf("            {\n")
    printf("                keysum = unicodeToKeysymMap.get(unicode);\n")
    printf("            }\n")
    printf("            else\n")
    printf("            {\n")
    printf("                keysum = unicode + KeySym_base;\n")
    printf("            }\n")
    printf("        }\n")
    printf("        return keysum;\n")
    printf("    }\n")
    printf("\n")

}


