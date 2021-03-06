/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.misc;

import javax.management.RuntimeErrorException;

import com.tsc9526.monalisa.tools.string.MelpString;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpException {
 
	public static <T> T throwRuntimeException(Throwable e){
		if(e instanceof Error){
			throw new RuntimeErrorException((Error)e);
		}else if(e instanceof RuntimeException){
			throw (RuntimeException)e;
		}else{
			throw new RuntimeException(e);
		}
	}
	
	
	public static Throwable getCause(Throwable throwable) {
		Throwable cause = throwable;
		while (cause != null && cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}

	public static String toString(Throwable t) {
		return MelpString.toString(t);
	}
}
