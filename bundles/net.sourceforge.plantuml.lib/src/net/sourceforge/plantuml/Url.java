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
package net.sourceforge.plantuml;

import java.util.Comparator;

import net.sourceforge.plantuml.cucadiagram.dot.DotMaker2;
import net.sourceforge.plantuml.project.lang.Complement;

public class Url implements EnsureVisible, Complement {

	private final String url;
	private final String tooltip;
	private final String label;
	private boolean member;

	public Url(String url, String tooltip) {
		this(url, tooltip, null);
	}

	public Url(String url, String tooltip, String label) {
		if (url.contains("{")) {
			throw new IllegalArgumentException(url);
		}
		url = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(url, "\"");
		this.url = url;
		if (tooltip == null) {
			this.tooltip = url;
		} else {
			this.tooltip = BackSlash.manageNewLine(tooltip);
		}
		if (label == null || label.length() == 0) {
			this.label = url;
		} else {
			this.label = label;
		}
	}

	public static boolean isLatex(String pendingUrl) {
		return pendingUrl.startsWith("latex://");
	}

	public boolean isLatex() {
		return isLatex(url);
	}

	public final String getUrl() {
		return url;
	}

	public final String getTooltip() {
		return tooltip;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return super.toString() + " " + url + " " + visible.getCoords(1.0);
	}

	public String getCoords(double scale) {
		if (DotMaker2.isJunit() && visible.getCoords(1.0).contains("0,0,0,0")) {
			throw new IllegalStateException(toString());
		}
		return visible.getCoords(scale);
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public final boolean isMember() {
		return member;
	}

	private final BasicEnsureVisible visible = new BasicEnsureVisible();

	public void ensureVisible(double x, double y) {
		visible.ensureVisible(x, y);
	}

	public boolean hasData() {
		return visible.hasData();
	}

	public static final Comparator<Url> SURFACE_COMPARATOR = new Comparator<Url>() {
		public int compare(Url url1, Url url2) {
			final double surface1 = url1.visible.getSurface();
			final double surface2 = url2.visible.getSurface();
			if (surface1 > surface2) {
				return 1;
			} else if (surface1 < surface2) {
				return -1;
			}
			return 0;
		}
	};

}