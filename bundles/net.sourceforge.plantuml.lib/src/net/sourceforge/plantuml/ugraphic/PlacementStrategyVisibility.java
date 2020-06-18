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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class PlacementStrategyVisibility extends AbstractPlacementStrategy {

	private final int col2;

	public PlacementStrategyVisibility(StringBounder stringBounder, int col2) {
		super(stringBounder);
		this.col2 = col2;
	}

	public Map<TextBlock, Point2D> getPositions(double width, double height) {
		final Map<TextBlock, Point2D> result = new LinkedHashMap<TextBlock, Point2D>();
		double y = 0;
		for (final Iterator<Map.Entry<TextBlock, Dimension2D>> it = getDimensions().entrySet().iterator(); it.hasNext();) {
			final Map.Entry<TextBlock, Dimension2D> ent1 = it.next();
			final Map.Entry<TextBlock, Dimension2D> ent2 = it.next();
			final double height1 = ent1.getValue().getHeight();
			final double height2 = ent2.getValue().getHeight();
			final double maxHeight12 = Math.max(height1, height2);
			// result.put(ent1.getKey(), new Point2D.Double(0, 2 + y + (maxHeight - height1) / 2));
			result.put(ent1.getKey(), new Point2D.Double(0, 2 + y));
			result.put(ent2.getKey(), new Point2D.Double(col2, y + (maxHeight12 - height2) / 2));
			y += maxHeight12;
		}
		return result;
	}

}
