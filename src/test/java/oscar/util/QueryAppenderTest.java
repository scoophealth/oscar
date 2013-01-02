/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueryAppenderTest {

	@Test
	public void testAppender() {
		QueryAppender q = new QueryAppender();
		assertEquals(q.toString(), "");

		q.setBaseQuery("[base query]");
		assertEquals(q.toString(), "[base query]");

		q.addWhere("[where clause]");
		assertEquals(q.toString(), "[base query] WHERE [where clause]");

		q.and("[all clause]");
		assertEquals(q.toString(), "[base query] WHERE [where clause] AND [all clause]");

		q.or("[another where clause]");
		assertEquals(q.toString(), "[base query] WHERE [where clause] AND [all clause] OR [another where clause]");

		q.addOrder("[order clause]");
		assertEquals(q.toString(), "[base query] WHERE [where clause] AND [all clause] OR [another where clause] ORDER BY [order clause]");
	}
	
	@Test
	public void testSubclauses() {
		QueryAppender q = new QueryAppender();
		q.setBaseQuery("[base query]");
		
		QueryAppender qq = new QueryAppender();
		qq.and("[subclause1]");
		qq.and("[subclause2]");
		qq.and("[subclause3]");
		
		q.and(qq);
		q.or("[clause3]");
		assertEquals("[base query] WHERE ([subclause1] AND [subclause2] AND [subclause3]) OR [clause3]", q.toString());
		
		q = new QueryAppender();
		q.setBaseQuery("[base query]");

		q.or("[clause1]");
		
		qq = new QueryAppender();
		qq.and("[subclause1]");
		qq.and("[subclause2]");
		qq.and("[subclause3]");
		q.and(qq);
		
		assertEquals("[base query] WHERE [clause1] AND ([subclause1] AND [subclause2] AND [subclause3])", q.toString());
	}
}
