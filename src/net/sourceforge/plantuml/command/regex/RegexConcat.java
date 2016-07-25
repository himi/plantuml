/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.command.regex;

import java.util.regex.Pattern;

public class RegexConcat extends RegexComposed implements IRegex {

	private final Pattern2 full;

	public RegexConcat(IRegex... partial) {
		super(partial);
		final StringBuilder sb = new StringBuilder();
		for (IRegex p : partial) {
			sb.append(p.getPattern());
		}
		this.full = MyPattern.cmpileNockeck(sb.toString(), Pattern.CASE_INSENSITIVE);
	}

	@Override
	protected Pattern2 getFull() {
		return full;
	}

}