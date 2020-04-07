/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml.tim;

public enum TLineType {

	PLAIN, AFFECTATION_DEFINE, AFFECTATION, ASSERT, IF, IFDEF, UNDEF, IFNDEF, ELSE, ELSEIF, ENDIF, WHILE, ENDWHILE,
	FOREACH, ENDFOREACH, DECLARE_FUNCTION, END_FUNCTION, RETURN, LEGACY_DEFINE, LEGACY_DEFINELONG, INCLUDE, INCLUDE_DEF,
	IMPORT, STARTSUB, ENDSUB, INCLUDESUB, LOG, DUMP_MEMORY, COMMENT_SIMPLE, COMMENT_LONG_START;

//	private boolean elseLike() {
//		return this == ELSE || this == ELSEIF;
//	}
//
//	public boolean incIndentAfter() {
//		return this == IF || this == IFDEF || this == IFNDEF || elseLike();
//	}
//
//	public boolean decIndentBefore() {
//		return this == ENDIF || elseLike();
//	}

	public static TLineType getFromLineInternal(String s) {
		if (s.matches("^\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\(.*")) {
			return LEGACY_DEFINE;
		}
		if (s.matches("^\\s*!definelong\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*")) {
			return LEGACY_DEFINELONG;
		}
		if (s.matches("^\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*")) {
			return AFFECTATION_DEFINE;
		}
		if (s.matches("^\\s*!\\s*(local|global)?\\s*\\$?[\\p{L}_][\\p{L}_0-9]*\\s*=.*")) {
			return AFFECTATION;
		}
		if (s.matches("^\\s*'.*")) {
			return COMMENT_SIMPLE;
		}
		if (s.matches("^\\s*/'.*'/\\s*$")) {
			return COMMENT_SIMPLE;
		}
		if (s.matches("^\\s*/'.*") && s.contains("'/") == false) {
			return COMMENT_LONG_START;
		}
		if (s.matches("^\\s*!ifdef\\s+.*")) {
			return IFDEF;
		}
		if (s.matches("^\\s*!undef\\s+.*")) {
			return UNDEF;
		}
		if (s.matches("^\\s*!ifndef\\s+.*")) {
			return IFNDEF;
		}
		if (s.matches("^\\s*!assert\\s+.*")) {
			return ASSERT;
		}
		if (s.matches("^\\s*!if\\s+.*")) {
			return IF;
		}
		if (s.matches("^\\s*!(unquoted\\s|final\\s)*function\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*")) {
			return DECLARE_FUNCTION;
		}
		if (s.matches("^\\s*!else\\b.*")) {
			return ELSE;
		}
		if (s.matches("^\\s*!elseif\\b.*")) {
			return ELSEIF;
		}
		if (s.matches("^\\s*!endif\\b.*")) {
			return ENDIF;
		}
		if (s.matches("^\\s*!while\\s+.*")) {
			return WHILE;
		}
		if (s.matches("^\\s*!endwhile\\b.*")) {
			return ENDWHILE;
		}
		if (s.matches("^\\s*!foreach\\s+.*")) {
			return FOREACH;
		}
		if (s.matches("^\\s*!endfor\\b.*")) {
			return ENDFOREACH;
		}
		if (s.matches("^\\s*!(endfunction|enddefinelong)\\b.*")) {
			return END_FUNCTION;
		}
		if (s.matches("^\\s*!return\\b.*")) {
			return RETURN;
		}
		if (s.matches("^\\s*!(include|includeurl|include_many|include_once)\\b.*")) {
			return INCLUDE;
		}
		if (s.matches("^\\s*!(includedef)\\b.*")) {
			return INCLUDE_DEF;
		}
		if (s.matches("^\\s*!(import)\\b.*")) {
			return IMPORT;
		}
		if (s.matches("^\\s*!startsub\\s+.*")) {
			return STARTSUB;
		}
		if (s.matches("^\\s*!endsub\\b.*")) {
			return ENDSUB;
		}
		if (s.matches("^\\s*!includesub\\b.*")) {
			return INCLUDESUB;
		}
		if (s.matches("^\\s*!(log)\\b.*")) {
			return LOG;
		}
		if (s.matches("^\\s*!(dump_memory)\\b.*")) {
			return DUMP_MEMORY;
		}
		return PLAIN;
	}

	public static boolean isQuote(char ch) {
		return ch == '\"' || ch == '\'';
	}

	public static boolean isLetterOrUnderscoreOrDigit(char ch) {
		return isLetterOrUnderscore(ch) || isLatinDigit(ch);
	}

	public static boolean isLetterOrUnderscore(char ch) {
		return isLetter(ch) || ch == '_';
	}

	public static boolean isLetterOrUnderscoreOrDollar(char ch) {
		return isLetterOrUnderscore(ch) || ch == '$';
	}

	public static boolean isLetterOrDigit(char ch) {
		return isLetter(ch) || isLatinDigit(ch);
	}

	public static boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

	public static boolean isSpaceChar(char ch) {
		return Character.isSpaceChar(ch);
	}

	public static boolean isLatinDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

}
