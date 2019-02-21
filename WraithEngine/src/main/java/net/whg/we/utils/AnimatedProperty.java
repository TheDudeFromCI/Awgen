package net.whg.we.utils;

public class AnimatedProperty
{
	private float _goalValue;
	private float _currentValue;
	private float _speed = 1f;

	public AnimatedProperty(float currentValue)
	{
		_goalValue = currentValue;
		_currentValue = currentValue;
	}

	public float getSpeed()
	{
		return _speed;
	}

	public void setSpeed(float speed)
	{
		_speed = speed;
	}

	public float getGoalValue()
	{
		return _goalValue;
	}

	public void setGoalValue(float goalValue)
	{
		_goalValue = goalValue;
	}

	public float update(float time)
	{
		if (_currentValue < _goalValue)
			_currentValue = Math.min(_goalValue, _currentValue + time / _speed);
		else
			_currentValue = Math.max(_goalValue, _currentValue - time / _speed);

		return _currentValue;
	}

	public float getValue()
	{
		return _currentValue;
	}
}
