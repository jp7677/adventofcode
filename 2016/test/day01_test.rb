# frozen_string_literal: true

require 'minitest/autorun'
require_relative 'util'

class Directions
  NORTH = 1
  EAST = 2
  SOUTH = 3
  WEST = 4

  def self.turn_right(direction)
    case direction
    when NORTH then EAST
    when EAST then SOUTH
    when SOUTH then WEST
    when WEST then NORTH
    else raise('invalid value')
    end
  end

  def self.turn_left(direction)
    case direction
    when NORTH then WEST
    when WEST then SOUTH
    when SOUTH then EAST
    when EAST then NORTH
    else raise('invalid value')
    end
  end
end

describe 'day01' do
  it 'solves part 01' do
    steps = Util.read_input('day01-input.txt').first.split(', ')

    result = steps.reduce [Directions::NORTH, [0, 0]] do |acc, it|
      direction =
        case it[0]
        when 'R' then Directions.turn_right(acc[0])
        when 'L'then Directions.turn_left(acc[0])
        else raise('invalid value')
        end

      step_numbers = it[1..].to_i
      case direction
      when Directions::NORTH then [direction, [acc[1][0] + step_numbers, acc[1][1]]]
      when Directions::EAST then [direction, [acc[1][0], acc[1][1] + step_numbers]]
      when Directions::SOUTH then [direction, [acc[1][0] - step_numbers, acc[1][1]]]
      when Directions::WEST then [direction, [acc[1][0], acc[1][1] - step_numbers]]
      else raise('invalid value')
      end
    end

    assert_equal 279, result[1][0] + result[1][1]
  end
end
