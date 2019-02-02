package org.hacksmu.pickmeup.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Pair<L, R> implements Serializable
{
	private L _left;
	private R _right;

	public static <L, R> Pair<L, R> create(L left, R right)
	{
		return new Pair<L, R>(left, right);
	}

	private Pair(L left, R right)
	{
		setLeft(left);
		setRight(right);
	}

	public L getLeft()
	{
		return _left;
	}

	public void setLeft(L left)
	{
		_left = left;
	}

	public R getRight()
	{
		return _right;
	}

	public void setRight(R right)
	{
		_right = right;
	}

	@Override
	public String toString()
	{
		return getLeft().toString() + ":" + getRight().toString();
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pair<?, ?> pair = (Pair<?, ?>) o;

		if (_left != null ? !_left.equals(pair._left) : pair._left != null) return false;
		return _right != null ? _right.equals(pair._right) : pair._right == null;
	}

	@Override
	public int hashCode()
	{
		int result = _left != null ? _left.hashCode() : 0;
		result = 31 * result + (_right != null ? _right.hashCode() : 0);
		return result;
	}
}