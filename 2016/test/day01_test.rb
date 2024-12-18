# frozen_string_literal: true

require 'minitest/autorun'
require_relative 'util'

class Directions
  NORTH = 1
  EAST = 2
  SOUTH = 3
  WEST = 4

  def self.turn_right(direction) # rubocop:disable Metrics/MethodLength
    case direction
    when NORTH
      EAST
    when EAST
      SOUTH
    when SOUTH
      WEST
    when WEST
      NORTH
    else
      raise('invalid value')
    end
  end

  def self.turn_left(direction) # rubocop:disable Metrics/MethodLength
    case direction
    when NORTH
      WEST
    when WEST
      SOUTH
    when SOUTH
      EAST
    when EAST
      NORTH
    else
      raise('invalid value')
    end
  end
end

describe 'day01' do # rubocop:disable Metrics/BlockLength
  it 'solves part 01' do # rubocop:disable Metrics/BlockLength
    steps = Util.read_input('day01-input.txt').first.split(', ')

    result = steps.reduce [Directions::NORTH, [0, 0]] do |acc, it|
      direction =
        case it[0]
        when 'R'
          Directions.turn_right(acc[0])
        when 'L'
          Directions.turn_left(acc[0])
        else
          raise('invalid value')
        end

      step_numbers = it[1..].to_i
      case direction
      when Directions::NORTH
        [direction, [acc[1][0] + step_numbers, acc[1][1]]]
      when Directions::EAST
        [direction, [acc[1][0], acc[1][1] + step_numbers]]
      when Directions::SOUTH
        [direction, [acc[1][0] - step_numbers, acc[1][1]]]
      when Directions::WEST
        [direction, [acc[1][0], acc[1][1] - step_numbers]]
      else
        raise('invalid value')
      end
    end

    assert_equal 279, result[1][0] + result[1][1]
  end
end
