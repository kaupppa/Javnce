/* * Copyright (C) 2013  Pauli Kauppinen
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

package org.javnce.vnc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * The key code mapping class.
 *
 * Generated file, do not edit manually.
 * Generated with command : tools/generate_keymappingclass.awk gawk /usr/include/X11/keysymdef.h
 */
public class KeyMap<T> {

    /**
     * The unicode to keysym map.
     */
    static final private Map<Integer, Integer> unicodeToKeysymMap = createUnicodeMap(); //Unicode as key and keysym as value
    /**
     * The custom map.
     */
    final private Map<T, Integer> map;

    public KeyMap() {
        this.map = new HashMap<>();
    }

    public static final int None = 0x0;
    public static final int KeySym_base = 0x1000000;

    //Next are ASCII constants. See keysymdef.h
    public static final int XK_space = 0x20;
    public static final int XK_exclam = 0x21;
    public static final int XK_quotedbl = 0x22;
    public static final int XK_numbersign = 0x23;
    public static final int XK_dollar = 0x24;
    public static final int XK_percent = 0x25;
    public static final int XK_ampersand = 0x26;
    public static final int XK_apostrophe = 0x27;
    public static final int XK_parenleft = 0x28;
    public static final int XK_parenright = 0x29;
    public static final int XK_asterisk = 0x2a;
    public static final int XK_plus = 0x2b;
    public static final int XK_comma = 0x2c;
    public static final int XK_minus = 0x2d;
    public static final int XK_period = 0x2e;
    public static final int XK_slash = 0x2f;
    public static final int XK_0 = 0x30;
    public static final int XK_1 = 0x31;
    public static final int XK_2 = 0x32;
    public static final int XK_3 = 0x33;
    public static final int XK_4 = 0x34;
    public static final int XK_5 = 0x35;
    public static final int XK_6 = 0x36;
    public static final int XK_7 = 0x37;
    public static final int XK_8 = 0x38;
    public static final int XK_9 = 0x39;
    public static final int XK_colon = 0x3a;
    public static final int XK_semicolon = 0x3b;
    public static final int XK_less = 0x3c;
    public static final int XK_equal = 0x3d;
    public static final int XK_greater = 0x3e;
    public static final int XK_question = 0x3f;
    public static final int XK_at = 0x40;
    public static final int XK_A = 0x41;
    public static final int XK_B = 0x42;
    public static final int XK_C = 0x43;
    public static final int XK_D = 0x44;
    public static final int XK_E = 0x45;
    public static final int XK_F = 0x46;
    public static final int XK_G = 0x47;
    public static final int XK_H = 0x48;
    public static final int XK_I = 0x49;
    public static final int XK_J = 0x4a;
    public static final int XK_K = 0x4b;
    public static final int XK_L = 0x4c;
    public static final int XK_M = 0x4d;
    public static final int XK_N = 0x4e;
    public static final int XK_O = 0x4f;
    public static final int XK_P = 0x50;
    public static final int XK_Q = 0x51;
    public static final int XK_R = 0x52;
    public static final int XK_S = 0x53;
    public static final int XK_T = 0x54;
    public static final int XK_U = 0x55;
    public static final int XK_V = 0x56;
    public static final int XK_W = 0x57;
    public static final int XK_X = 0x58;
    public static final int XK_Y = 0x59;
    public static final int XK_Z = 0x5a;
    public static final int XK_bracketleft = 0x5b;
    public static final int XK_backslash = 0x5c;
    public static final int XK_bracketright = 0x5d;
    public static final int XK_asciicircum = 0x5e;
    public static final int XK_underscore = 0x5f;
    public static final int XK_grave = 0x60;
    public static final int XK_a = 0x61;
    public static final int XK_b = 0x62;
    public static final int XK_c = 0x63;
    public static final int XK_d = 0x64;
    public static final int XK_e = 0x65;
    public static final int XK_f = 0x66;
    public static final int XK_g = 0x67;
    public static final int XK_h = 0x68;
    public static final int XK_i = 0x69;
    public static final int XK_j = 0x6a;
    public static final int XK_k = 0x6b;
    public static final int XK_l = 0x6c;
    public static final int XK_m = 0x6d;
    public static final int XK_n = 0x6e;
    public static final int XK_o = 0x6f;
    public static final int XK_p = 0x70;
    public static final int XK_q = 0x71;
    public static final int XK_r = 0x72;
    public static final int XK_s = 0x73;
    public static final int XK_t = 0x74;
    public static final int XK_u = 0x75;
    public static final int XK_v = 0x76;
    public static final int XK_w = 0x77;
    public static final int XK_x = 0x78;
    public static final int XK_y = 0x79;
    public static final int XK_z = 0x7a;
    public static final int XK_braceleft = 0x7b;
    public static final int XK_bar = 0x7c;
    public static final int XK_braceright = 0x7d;
    public static final int XK_asciitilde = 0x7e;
    public static final int XK_nobreakspace = 0xa0;
    public static final int XK_exclamdown = 0xa1;
    public static final int XK_cent = 0xa2;
    public static final int XK_sterling = 0xa3;
    public static final int XK_currency = 0xa4;
    public static final int XK_yen = 0xa5;
    public static final int XK_brokenbar = 0xa6;
    public static final int XK_section = 0xa7;
    public static final int XK_diaeresis = 0xa8;
    public static final int XK_copyright = 0xa9;
    public static final int XK_ordfeminine = 0xaa;
    public static final int XK_guillemotleft = 0xab;
    public static final int XK_notsign = 0xac;
    public static final int XK_hyphen = 0xad;
    public static final int XK_registered = 0xae;
    public static final int XK_macron = 0xaf;
    public static final int XK_degree = 0xb0;
    public static final int XK_plusminus = 0xb1;
    public static final int XK_twosuperior = 0xb2;
    public static final int XK_threesuperior = 0xb3;
    public static final int XK_acute = 0xb4;
    public static final int XK_mu = 0xb5;
    public static final int XK_paragraph = 0xb6;
    public static final int XK_periodcentered = 0xb7;
    public static final int XK_cedilla = 0xb8;
    public static final int XK_onesuperior = 0xb9;
    public static final int XK_masculine = 0xba;
    public static final int XK_guillemotright = 0xbb;
    public static final int XK_onequarter = 0xbc;
    public static final int XK_onehalf = 0xbd;
    public static final int XK_threequarters = 0xbe;
    public static final int XK_questiondown = 0xbf;
    public static final int XK_Agrave = 0xc0;
    public static final int XK_Aacute = 0xc1;
    public static final int XK_Acircumflex = 0xc2;
    public static final int XK_Atilde = 0xc3;
    public static final int XK_Adiaeresis = 0xc4;
    public static final int XK_Aring = 0xc5;
    public static final int XK_AE = 0xc6;
    public static final int XK_Ccedilla = 0xc7;
    public static final int XK_Egrave = 0xc8;
    public static final int XK_Eacute = 0xc9;
    public static final int XK_Ecircumflex = 0xca;
    public static final int XK_Ediaeresis = 0xcb;
    public static final int XK_Igrave = 0xcc;
    public static final int XK_Iacute = 0xcd;
    public static final int XK_Icircumflex = 0xce;
    public static final int XK_Idiaeresis = 0xcf;
    public static final int XK_ETH = 0xd0;
    public static final int XK_Ntilde = 0xd1;
    public static final int XK_Ograve = 0xd2;
    public static final int XK_Oacute = 0xd3;
    public static final int XK_Ocircumflex = 0xd4;
    public static final int XK_Otilde = 0xd5;
    public static final int XK_Odiaeresis = 0xd6;
    public static final int XK_multiply = 0xd7;
    public static final int XK_Oslash = 0xd8;
    public static final int XK_Ooblique = 0xd8;
    public static final int XK_Ugrave = 0xd9;
    public static final int XK_Uacute = 0xda;
    public static final int XK_Ucircumflex = 0xdb;
    public static final int XK_Udiaeresis = 0xdc;
    public static final int XK_Yacute = 0xdd;
    public static final int XK_THORN = 0xde;
    public static final int XK_ssharp = 0xdf;
    public static final int XK_agrave = 0xe0;
    public static final int XK_aacute = 0xe1;
    public static final int XK_acircumflex = 0xe2;
    public static final int XK_atilde = 0xe3;
    public static final int XK_adiaeresis = 0xe4;
    public static final int XK_aring = 0xe5;
    public static final int XK_ae = 0xe6;
    public static final int XK_ccedilla = 0xe7;
    public static final int XK_egrave = 0xe8;
    public static final int XK_eacute = 0xe9;
    public static final int XK_ecircumflex = 0xea;
    public static final int XK_ediaeresis = 0xeb;
    public static final int XK_igrave = 0xec;
    public static final int XK_iacute = 0xed;
    public static final int XK_icircumflex = 0xee;
    public static final int XK_idiaeresis = 0xef;
    public static final int XK_eth = 0xf0;
    public static final int XK_ntilde = 0xf1;
    public static final int XK_ograve = 0xf2;
    public static final int XK_oacute = 0xf3;
    public static final int XK_ocircumflex = 0xf4;
    public static final int XK_otilde = 0xf5;
    public static final int XK_odiaeresis = 0xf6;
    public static final int XK_division = 0xf7;
    public static final int XK_oslash = 0xf8;
    public static final int XK_ooblique = 0xf8;
    public static final int XK_ugrave = 0xf9;
    public static final int XK_uacute = 0xfa;
    public static final int XK_ucircumflex = 0xfb;
    public static final int XK_udiaeresis = 0xfc;
    public static final int XK_yacute = 0xfd;
    public static final int XK_thorn = 0xfe;
    public static final int XK_ydiaeresis = 0xff;

    //Next are none unicode constants. See keysymdef.h
    public static final int XK_BackSpace = 0xff08;
    public static final int XK_Tab = 0xff09;
    public static final int XK_Linefeed = 0xff0a;
    public static final int XK_Clear = 0xff0b;
    public static final int XK_Return = 0xff0d;
    public static final int XK_Pause = 0xff13;
    public static final int XK_Scroll_Lock = 0xff14;
    public static final int XK_Sys_Req = 0xff15;
    public static final int XK_Escape = 0xff1b;
    public static final int XK_Delete = 0xffff;
    public static final int XK_Multi_key = 0xff20;
    public static final int XK_Codeinput = 0xff37;
    public static final int XK_SingleCandidate = 0xff3c;
    public static final int XK_MultipleCandidate = 0xff3d;
    public static final int XK_PreviousCandidate = 0xff3e;
    public static final int XK_Kanji = 0xff21;
    public static final int XK_Muhenkan = 0xff22;
    public static final int XK_Henkan_Mode = 0xff23;
    public static final int XK_Henkan = 0xff23;
    public static final int XK_Romaji = 0xff24;
    public static final int XK_Hiragana = 0xff25;
    public static final int XK_Katakana = 0xff26;
    public static final int XK_Hiragana_Katakana = 0xff27;
    public static final int XK_Zenkaku = 0xff28;
    public static final int XK_Hankaku = 0xff29;
    public static final int XK_Zenkaku_Hankaku = 0xff2a;
    public static final int XK_Touroku = 0xff2b;
    public static final int XK_Massyo = 0xff2c;
    public static final int XK_Kana_Lock = 0xff2d;
    public static final int XK_Kana_Shift = 0xff2e;
    public static final int XK_Eisu_Shift = 0xff2f;
    public static final int XK_Eisu_toggle = 0xff30;
    public static final int XK_Kanji_Bangou = 0xff37;
    public static final int XK_Zen_Koho = 0xff3d;
    public static final int XK_Mae_Koho = 0xff3e;
    public static final int XK_Home = 0xff50;
    public static final int XK_Left = 0xff51;
    public static final int XK_Up = 0xff52;
    public static final int XK_Right = 0xff53;
    public static final int XK_Down = 0xff54;
    public static final int XK_Prior = 0xff55;
    public static final int XK_Page_Up = 0xff55;
    public static final int XK_Next = 0xff56;
    public static final int XK_Page_Down = 0xff56;
    public static final int XK_End = 0xff57;
    public static final int XK_Begin = 0xff58;
    public static final int XK_Select = 0xff60;
    public static final int XK_Print = 0xff61;
    public static final int XK_Execute = 0xff62;
    public static final int XK_Insert = 0xff63;
    public static final int XK_Undo = 0xff65;
    public static final int XK_Redo = 0xff66;
    public static final int XK_Menu = 0xff67;
    public static final int XK_Find = 0xff68;
    public static final int XK_Cancel = 0xff69;
    public static final int XK_Help = 0xff6a;
    public static final int XK_Break = 0xff6b;
    public static final int XK_Mode_switch = 0xff7e;
    public static final int XK_script_switch = 0xff7e;
    public static final int XK_Num_Lock = 0xff7f;
    public static final int XK_KP_Space = 0xff80;
    public static final int XK_KP_Tab = 0xff89;
    public static final int XK_KP_Enter = 0xff8d;
    public static final int XK_KP_F1 = 0xff91;
    public static final int XK_KP_F2 = 0xff92;
    public static final int XK_KP_F3 = 0xff93;
    public static final int XK_KP_F4 = 0xff94;
    public static final int XK_KP_Home = 0xff95;
    public static final int XK_KP_Left = 0xff96;
    public static final int XK_KP_Up = 0xff97;
    public static final int XK_KP_Right = 0xff98;
    public static final int XK_KP_Down = 0xff99;
    public static final int XK_KP_Prior = 0xff9a;
    public static final int XK_KP_Page_Up = 0xff9a;
    public static final int XK_KP_Next = 0xff9b;
    public static final int XK_KP_Page_Down = 0xff9b;
    public static final int XK_KP_End = 0xff9c;
    public static final int XK_KP_Begin = 0xff9d;
    public static final int XK_KP_Insert = 0xff9e;
    public static final int XK_KP_Delete = 0xff9f;
    public static final int XK_KP_Equal = 0xffbd;
    public static final int XK_KP_Multiply = 0xffaa;
    public static final int XK_KP_Add = 0xffab;
    public static final int XK_KP_Separator = 0xffac;
    public static final int XK_KP_Subtract = 0xffad;
    public static final int XK_KP_Decimal = 0xffae;
    public static final int XK_KP_Divide = 0xffaf;
    public static final int XK_KP_0 = 0xffb0;
    public static final int XK_KP_1 = 0xffb1;
    public static final int XK_KP_2 = 0xffb2;
    public static final int XK_KP_3 = 0xffb3;
    public static final int XK_KP_4 = 0xffb4;
    public static final int XK_KP_5 = 0xffb5;
    public static final int XK_KP_6 = 0xffb6;
    public static final int XK_KP_7 = 0xffb7;
    public static final int XK_KP_8 = 0xffb8;
    public static final int XK_KP_9 = 0xffb9;
    public static final int XK_F1 = 0xffbe;
    public static final int XK_F2 = 0xffbf;
    public static final int XK_F3 = 0xffc0;
    public static final int XK_F4 = 0xffc1;
    public static final int XK_F5 = 0xffc2;
    public static final int XK_F6 = 0xffc3;
    public static final int XK_F7 = 0xffc4;
    public static final int XK_F8 = 0xffc5;
    public static final int XK_F9 = 0xffc6;
    public static final int XK_F10 = 0xffc7;
    public static final int XK_F11 = 0xffc8;
    public static final int XK_L1 = 0xffc8;
    public static final int XK_F12 = 0xffc9;
    public static final int XK_L2 = 0xffc9;
    public static final int XK_F13 = 0xffca;
    public static final int XK_L3 = 0xffca;
    public static final int XK_F14 = 0xffcb;
    public static final int XK_L4 = 0xffcb;
    public static final int XK_F15 = 0xffcc;
    public static final int XK_L5 = 0xffcc;
    public static final int XK_F16 = 0xffcd;
    public static final int XK_L6 = 0xffcd;
    public static final int XK_F17 = 0xffce;
    public static final int XK_L7 = 0xffce;
    public static final int XK_F18 = 0xffcf;
    public static final int XK_L8 = 0xffcf;
    public static final int XK_F19 = 0xffd0;
    public static final int XK_L9 = 0xffd0;
    public static final int XK_F20 = 0xffd1;
    public static final int XK_L10 = 0xffd1;
    public static final int XK_F21 = 0xffd2;
    public static final int XK_R1 = 0xffd2;
    public static final int XK_F22 = 0xffd3;
    public static final int XK_R2 = 0xffd3;
    public static final int XK_F23 = 0xffd4;
    public static final int XK_R3 = 0xffd4;
    public static final int XK_F24 = 0xffd5;
    public static final int XK_R4 = 0xffd5;
    public static final int XK_F25 = 0xffd6;
    public static final int XK_R5 = 0xffd6;
    public static final int XK_F26 = 0xffd7;
    public static final int XK_R6 = 0xffd7;
    public static final int XK_F27 = 0xffd8;
    public static final int XK_R7 = 0xffd8;
    public static final int XK_F28 = 0xffd9;
    public static final int XK_R8 = 0xffd9;
    public static final int XK_F29 = 0xffda;
    public static final int XK_R9 = 0xffda;
    public static final int XK_F30 = 0xffdb;
    public static final int XK_R10 = 0xffdb;
    public static final int XK_F31 = 0xffdc;
    public static final int XK_R11 = 0xffdc;
    public static final int XK_F32 = 0xffdd;
    public static final int XK_R12 = 0xffdd;
    public static final int XK_F33 = 0xffde;
    public static final int XK_R13 = 0xffde;
    public static final int XK_F34 = 0xffdf;
    public static final int XK_R14 = 0xffdf;
    public static final int XK_F35 = 0xffe0;
    public static final int XK_R15 = 0xffe0;
    public static final int XK_Shift_L = 0xffe1;
    public static final int XK_Shift_R = 0xffe2;
    public static final int XK_Control_L = 0xffe3;
    public static final int XK_Control_R = 0xffe4;
    public static final int XK_Caps_Lock = 0xffe5;
    public static final int XK_Shift_Lock = 0xffe6;
    public static final int XK_Meta_L = 0xffe7;
    public static final int XK_Meta_R = 0xffe8;
    public static final int XK_Alt_L = 0xffe9;
    public static final int XK_Alt_R = 0xffea;
    public static final int XK_Super_L = 0xffeb;
    public static final int XK_Super_R = 0xffec;
    public static final int XK_Hyper_L = 0xffed;
    public static final int XK_Hyper_R = 0xffee;

    //Next are unicode constants to be mapped. See keysymdef.h
    public static final int XK_Aogonek = 0x1a1;
    public static final int XK_breve = 0x1a2;
    public static final int XK_Lstroke = 0x1a3;
    public static final int XK_Lcaron = 0x1a5;
    public static final int XK_Sacute = 0x1a6;
    public static final int XK_Scaron = 0x1a9;
    public static final int XK_Scedilla = 0x1aa;
    public static final int XK_Tcaron = 0x1ab;
    public static final int XK_Zacute = 0x1ac;
    public static final int XK_Zcaron = 0x1ae;
    public static final int XK_Zabovedot = 0x1af;
    public static final int XK_aogonek = 0x1b1;
    public static final int XK_ogonek = 0x1b2;
    public static final int XK_lstroke = 0x1b3;
    public static final int XK_lcaron = 0x1b5;
    public static final int XK_sacute = 0x1b6;
    public static final int XK_caron = 0x1b7;
    public static final int XK_scaron = 0x1b9;
    public static final int XK_scedilla = 0x1ba;
    public static final int XK_tcaron = 0x1bb;
    public static final int XK_zacute = 0x1bc;
    public static final int XK_doubleacute = 0x1bd;
    public static final int XK_zcaron = 0x1be;
    public static final int XK_zabovedot = 0x1bf;
    public static final int XK_Racute = 0x1c0;
    public static final int XK_Abreve = 0x1c3;
    public static final int XK_Lacute = 0x1c5;
    public static final int XK_Cacute = 0x1c6;
    public static final int XK_Ccaron = 0x1c8;
    public static final int XK_Eogonek = 0x1ca;
    public static final int XK_Ecaron = 0x1cc;
    public static final int XK_Dcaron = 0x1cf;
    public static final int XK_Dstroke = 0x1d0;
    public static final int XK_Nacute = 0x1d1;
    public static final int XK_Ncaron = 0x1d2;
    public static final int XK_Odoubleacute = 0x1d5;
    public static final int XK_Rcaron = 0x1d8;
    public static final int XK_Uring = 0x1d9;
    public static final int XK_Udoubleacute = 0x1db;
    public static final int XK_Tcedilla = 0x1de;
    public static final int XK_racute = 0x1e0;
    public static final int XK_abreve = 0x1e3;
    public static final int XK_lacute = 0x1e5;
    public static final int XK_cacute = 0x1e6;
    public static final int XK_ccaron = 0x1e8;
    public static final int XK_eogonek = 0x1ea;
    public static final int XK_ecaron = 0x1ec;
    public static final int XK_dcaron = 0x1ef;
    public static final int XK_dstroke = 0x1f0;
    public static final int XK_nacute = 0x1f1;
    public static final int XK_ncaron = 0x1f2;
    public static final int XK_odoubleacute = 0x1f5;
    public static final int XK_rcaron = 0x1f8;
    public static final int XK_uring = 0x1f9;
    public static final int XK_udoubleacute = 0x1fb;
    public static final int XK_tcedilla = 0x1fe;
    public static final int XK_abovedot = 0x1ff;
    public static final int XK_Hstroke = 0x2a1;
    public static final int XK_Hcircumflex = 0x2a6;
    public static final int XK_Iabovedot = 0x2a9;
    public static final int XK_Gbreve = 0x2ab;
    public static final int XK_Jcircumflex = 0x2ac;
    public static final int XK_hstroke = 0x2b1;
    public static final int XK_hcircumflex = 0x2b6;
    public static final int XK_idotless = 0x2b9;
    public static final int XK_gbreve = 0x2bb;
    public static final int XK_jcircumflex = 0x2bc;
    public static final int XK_Cabovedot = 0x2c5;
    public static final int XK_Ccircumflex = 0x2c6;
    public static final int XK_Gabovedot = 0x2d5;
    public static final int XK_Gcircumflex = 0x2d8;
    public static final int XK_Ubreve = 0x2dd;
    public static final int XK_Scircumflex = 0x2de;
    public static final int XK_cabovedot = 0x2e5;
    public static final int XK_ccircumflex = 0x2e6;
    public static final int XK_gabovedot = 0x2f5;
    public static final int XK_gcircumflex = 0x2f8;
    public static final int XK_ubreve = 0x2fd;
    public static final int XK_scircumflex = 0x2fe;
    public static final int XK_kra = 0x3a2;
    public static final int XK_Rcedilla = 0x3a3;
    public static final int XK_Itilde = 0x3a5;
    public static final int XK_Lcedilla = 0x3a6;
    public static final int XK_Emacron = 0x3aa;
    public static final int XK_Gcedilla = 0x3ab;
    public static final int XK_Tslash = 0x3ac;
    public static final int XK_rcedilla = 0x3b3;
    public static final int XK_itilde = 0x3b5;
    public static final int XK_lcedilla = 0x3b6;
    public static final int XK_emacron = 0x3ba;
    public static final int XK_gcedilla = 0x3bb;
    public static final int XK_tslash = 0x3bc;
    public static final int XK_ENG = 0x3bd;
    public static final int XK_eng = 0x3bf;
    public static final int XK_Amacron = 0x3c0;
    public static final int XK_Iogonek = 0x3c7;
    public static final int XK_Eabovedot = 0x3cc;
    public static final int XK_Imacron = 0x3cf;
    public static final int XK_Ncedilla = 0x3d1;
    public static final int XK_Omacron = 0x3d2;
    public static final int XK_Kcedilla = 0x3d3;
    public static final int XK_Uogonek = 0x3d9;
    public static final int XK_Utilde = 0x3dd;
    public static final int XK_Umacron = 0x3de;
    public static final int XK_amacron = 0x3e0;
    public static final int XK_iogonek = 0x3e7;
    public static final int XK_eabovedot = 0x3ec;
    public static final int XK_imacron = 0x3ef;
    public static final int XK_ncedilla = 0x3f1;
    public static final int XK_omacron = 0x3f2;
    public static final int XK_kcedilla = 0x3f3;
    public static final int XK_uogonek = 0x3f9;
    public static final int XK_utilde = 0x3fd;
    public static final int XK_umacron = 0x3fe;
    public static final int XK_OE = 0x13bc;
    public static final int XK_oe = 0x13bd;
    public static final int XK_Ydiaeresis = 0x13be;

    static private Map<Integer, Integer> createUnicodeMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(new Integer(0x104), new Integer(XK_Aogonek));
        map.put(new Integer(0x2D8), new Integer(XK_breve));
        map.put(new Integer(0x141), new Integer(XK_Lstroke));
        map.put(new Integer(0x13D), new Integer(XK_Lcaron));
        map.put(new Integer(0x15A), new Integer(XK_Sacute));
        map.put(new Integer(0x160), new Integer(XK_Scaron));
        map.put(new Integer(0x15E), new Integer(XK_Scedilla));
        map.put(new Integer(0x164), new Integer(XK_Tcaron));
        map.put(new Integer(0x179), new Integer(XK_Zacute));
        map.put(new Integer(0x17D), new Integer(XK_Zcaron));
        map.put(new Integer(0x17B), new Integer(XK_Zabovedot));
        map.put(new Integer(0x105), new Integer(XK_aogonek));
        map.put(new Integer(0x2DB), new Integer(XK_ogonek));
        map.put(new Integer(0x142), new Integer(XK_lstroke));
        map.put(new Integer(0x13E), new Integer(XK_lcaron));
        map.put(new Integer(0x15B), new Integer(XK_sacute));
        map.put(new Integer(0x2C7), new Integer(XK_caron));
        map.put(new Integer(0x161), new Integer(XK_scaron));
        map.put(new Integer(0x15F), new Integer(XK_scedilla));
        map.put(new Integer(0x165), new Integer(XK_tcaron));
        map.put(new Integer(0x17A), new Integer(XK_zacute));
        map.put(new Integer(0x2DD), new Integer(XK_doubleacute));
        map.put(new Integer(0x17E), new Integer(XK_zcaron));
        map.put(new Integer(0x17C), new Integer(XK_zabovedot));
        map.put(new Integer(0x154), new Integer(XK_Racute));
        map.put(new Integer(0x102), new Integer(XK_Abreve));
        map.put(new Integer(0x139), new Integer(XK_Lacute));
        map.put(new Integer(0x106), new Integer(XK_Cacute));
        map.put(new Integer(0x10C), new Integer(XK_Ccaron));
        map.put(new Integer(0x118), new Integer(XK_Eogonek));
        map.put(new Integer(0x11A), new Integer(XK_Ecaron));
        map.put(new Integer(0x10E), new Integer(XK_Dcaron));
        map.put(new Integer(0x110), new Integer(XK_Dstroke));
        map.put(new Integer(0x143), new Integer(XK_Nacute));
        map.put(new Integer(0x147), new Integer(XK_Ncaron));
        map.put(new Integer(0x150), new Integer(XK_Odoubleacute));
        map.put(new Integer(0x158), new Integer(XK_Rcaron));
        map.put(new Integer(0x16E), new Integer(XK_Uring));
        map.put(new Integer(0x170), new Integer(XK_Udoubleacute));
        map.put(new Integer(0x162), new Integer(XK_Tcedilla));
        map.put(new Integer(0x155), new Integer(XK_racute));
        map.put(new Integer(0x103), new Integer(XK_abreve));
        map.put(new Integer(0x13A), new Integer(XK_lacute));
        map.put(new Integer(0x107), new Integer(XK_cacute));
        map.put(new Integer(0x10D), new Integer(XK_ccaron));
        map.put(new Integer(0x119), new Integer(XK_eogonek));
        map.put(new Integer(0x11B), new Integer(XK_ecaron));
        map.put(new Integer(0x10F), new Integer(XK_dcaron));
        map.put(new Integer(0x111), new Integer(XK_dstroke));
        map.put(new Integer(0x144), new Integer(XK_nacute));
        map.put(new Integer(0x148), new Integer(XK_ncaron));
        map.put(new Integer(0x151), new Integer(XK_odoubleacute));
        map.put(new Integer(0x159), new Integer(XK_rcaron));
        map.put(new Integer(0x16F), new Integer(XK_uring));
        map.put(new Integer(0x171), new Integer(XK_udoubleacute));
        map.put(new Integer(0x163), new Integer(XK_tcedilla));
        map.put(new Integer(0x2D9), new Integer(XK_abovedot));
        map.put(new Integer(0x126), new Integer(XK_Hstroke));
        map.put(new Integer(0x124), new Integer(XK_Hcircumflex));
        map.put(new Integer(0x130), new Integer(XK_Iabovedot));
        map.put(new Integer(0x11E), new Integer(XK_Gbreve));
        map.put(new Integer(0x134), new Integer(XK_Jcircumflex));
        map.put(new Integer(0x127), new Integer(XK_hstroke));
        map.put(new Integer(0x125), new Integer(XK_hcircumflex));
        map.put(new Integer(0x131), new Integer(XK_idotless));
        map.put(new Integer(0x11F), new Integer(XK_gbreve));
        map.put(new Integer(0x135), new Integer(XK_jcircumflex));
        map.put(new Integer(0x10A), new Integer(XK_Cabovedot));
        map.put(new Integer(0x108), new Integer(XK_Ccircumflex));
        map.put(new Integer(0x120), new Integer(XK_Gabovedot));
        map.put(new Integer(0x11C), new Integer(XK_Gcircumflex));
        map.put(new Integer(0x16C), new Integer(XK_Ubreve));
        map.put(new Integer(0x15C), new Integer(XK_Scircumflex));
        map.put(new Integer(0x10B), new Integer(XK_cabovedot));
        map.put(new Integer(0x109), new Integer(XK_ccircumflex));
        map.put(new Integer(0x121), new Integer(XK_gabovedot));
        map.put(new Integer(0x11D), new Integer(XK_gcircumflex));
        map.put(new Integer(0x16D), new Integer(XK_ubreve));
        map.put(new Integer(0x15D), new Integer(XK_scircumflex));
        map.put(new Integer(0x138), new Integer(XK_kra));
        map.put(new Integer(0x156), new Integer(XK_Rcedilla));
        map.put(new Integer(0x128), new Integer(XK_Itilde));
        map.put(new Integer(0x13B), new Integer(XK_Lcedilla));
        map.put(new Integer(0x112), new Integer(XK_Emacron));
        map.put(new Integer(0x122), new Integer(XK_Gcedilla));
        map.put(new Integer(0x166), new Integer(XK_Tslash));
        map.put(new Integer(0x157), new Integer(XK_rcedilla));
        map.put(new Integer(0x129), new Integer(XK_itilde));
        map.put(new Integer(0x13C), new Integer(XK_lcedilla));
        map.put(new Integer(0x113), new Integer(XK_emacron));
        map.put(new Integer(0x123), new Integer(XK_gcedilla));
        map.put(new Integer(0x167), new Integer(XK_tslash));
        map.put(new Integer(0x14A), new Integer(XK_ENG));
        map.put(new Integer(0x14B), new Integer(XK_eng));
        map.put(new Integer(0x100), new Integer(XK_Amacron));
        map.put(new Integer(0x12E), new Integer(XK_Iogonek));
        map.put(new Integer(0x116), new Integer(XK_Eabovedot));
        map.put(new Integer(0x12A), new Integer(XK_Imacron));
        map.put(new Integer(0x145), new Integer(XK_Ncedilla));
        map.put(new Integer(0x14C), new Integer(XK_Omacron));
        map.put(new Integer(0x136), new Integer(XK_Kcedilla));
        map.put(new Integer(0x172), new Integer(XK_Uogonek));
        map.put(new Integer(0x168), new Integer(XK_Utilde));
        map.put(new Integer(0x16A), new Integer(XK_Umacron));
        map.put(new Integer(0x101), new Integer(XK_amacron));
        map.put(new Integer(0x12F), new Integer(XK_iogonek));
        map.put(new Integer(0x117), new Integer(XK_eabovedot));
        map.put(new Integer(0x12B), new Integer(XK_imacron));
        map.put(new Integer(0x146), new Integer(XK_ncedilla));
        map.put(new Integer(0x14D), new Integer(XK_omacron));
        map.put(new Integer(0x137), new Integer(XK_kcedilla));
        map.put(new Integer(0x173), new Integer(XK_uogonek));
        map.put(new Integer(0x169), new Integer(XK_utilde));
        map.put(new Integer(0x16B), new Integer(XK_umacron));
        map.put(new Integer(0x152), new Integer(XK_OE));
        map.put(new Integer(0x153), new Integer(XK_oe));
        map.put(new Integer(0x178), new Integer(XK_Ydiaeresis));
        return map;
    }

    /**
     * converts unicode value to X11 keysym value.
     *
     * @param unicode is the unicode value to be converted
     *
     * @return keysym value or None if not a unicode
     */
    static public int unicodeToKeySym(int unicode) {
        int keysum = None;

        if (0x20 <= unicode && unicode <= 0x7e) {
            keysum = unicode;
        } else if (0xa0 <= unicode && unicode <= 0xff) {
            keysum = unicode;
        } else if (0x100 <= unicode && unicode <= 0x10FFFF) {
            Integer key = new Integer(unicode);
            if (unicodeToKeysymMap.containsKey(key)) {
                keysum = unicodeToKeysymMap.get(key).intValue();
            } else {
                keysum = unicode + KeySym_base;
            }
        }
        return keysum;
    }


    /**
     * Adds keysym mapping.
     *
     * @param item is the value to be mapped
     * @param keysym is the mapped value of code
     */
    public void addMapping(T item, int keysym) {
        map.put(item, new Integer(keysym));
    }

    /**
     * Gets the mapped value.
     *
     * @param item is the value to be searched
     *
     * @return keysym value or None if not mapped
     */
    public int getMapped(T item) {
        int keysum = None;
        if (map.containsKey(item)) {
            keysum = map.get(item).intValue();
        }
        return keysum;
    }

}
