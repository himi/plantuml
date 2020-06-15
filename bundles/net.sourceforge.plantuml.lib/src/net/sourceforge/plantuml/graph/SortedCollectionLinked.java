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
package net.sourceforge.plantuml.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SortedCollectionLinked<S extends Comparable<S>> implements SortedCollection<S> {

	private final List<S> all = new LinkedList<S>();

	public Iterator<S> iterator() {
		return all.iterator();
	}

	public void add(S newEntry) {
		for (final ListIterator<S> it = all.listIterator(); it.hasNext();) {
			final S cur = it.next();
			if (cur.compareTo(newEntry) >= 0) {
				it.previous();
				it.add(newEntry);
				assert isSorted();
				return;
			}
		}
		all.add(newEntry);
		assert isSorted();
	}

	public int size() {
		return all.size();
	}

	List<S> toList() {
		return new ArrayList<S>(all);
	}

	boolean isSorted() {
		S before = null;
		for (S ent : all) {
			if (before != null && ent.compareTo(before) < 0) {
				return false;
			}
			before = ent;
		}
		return true;
	}

	public boolean contains(S entry) {
		return all.contains(entry);
	}

}
