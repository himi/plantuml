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
 * Revision $Revision: 3828 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.version.Version;

public class ProtectedCommand<S extends Diagram> implements Command<S> {

	private final Command<S> cmd;

	public ProtectedCommand(Command<S> cmd) {
		this.cmd = cmd;
	}

	public CommandExecutionResult execute(S system, BlocLines lines) {
		try {
			final CommandExecutionResult result = cmd.execute(system, lines);
			// if (result.isOk()) {
			// // TRACECOMMAND
			// System.err.println("CMD = " + cmd.getClass());
			// }
			return result;
		} catch (Throwable t) {
			Log.error("Error " + t);
			t.printStackTrace();
			String msg = "You should send a mail to plantuml@gmail.com or post to http://plantuml.com/qa with this log (V" + Version.versionString()
					+ ")";
			Log.error(msg);
			msg += " " + t.toString();
			return CommandExecutionResult.error(msg, t);
		}
	}

	public CommandControl isValid(BlocLines lines) {
		return cmd.isValid(lines);
	}

	public String[] getDescription() {
		return cmd.getDescription();
	}

}