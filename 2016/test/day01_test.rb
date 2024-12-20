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

Step = Data.define(:direction, :position)

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
      step_count = it[1..].to_i

      case direction
      when Directions::NORTH then [direction, [acc[1][0] + step_count, acc[1][1]]]
      when Directions::EAST then [direction, [acc[1][0], acc[1][1] + step_count]]
      when Directions::SOUTH then [direction, [acc[1][0] - step_count, acc[1][1]]]
      when Directions::WEST then [direction, [acc[1][0], acc[1][1] - step_count]]
      else raise('invalid value')
      end
    end

    assert_equal 279, result[1][0] + result[1][1]
  end

  it 'solves part 02' do
    steps = Util.read_input('day01-input.txt').first.split(', ')

    path = steps.reduce [Step.new(Directions::NORTH, [0, 0])] do |acc, it|
      prev = acc.last
      direction =
        case it[0]
        when 'R' then Directions.turn_right(prev.direction)
        when 'L'then Directions.turn_left(prev.direction)
        else raise('invalid value')
        end
      step_count = it[1..].to_i

      case direction
      when Directions::NORTH
        acc + (prev.position[0] + 1..prev.position[0] + step_count).map do |p|
          Step.new(direction, [p, prev.position[1]])
        end
      when Directions::EAST
        acc + (prev.position[1] + 1..prev.position[1] + step_count).map do |p|
          Step.new(direction, [prev.position[0], p])
        end
      when Directions::SOUTH
        acc + (prev.position[0] - 1..prev.position[0] - step_count).step(-1).map do |p|
          Step.new(direction, [p, prev.position[1]])
        end
      when Directions::WEST
        acc + (prev.position[1] - 1..prev.position[1] - step_count).step(-1).map do |p|
          Step.new(direction, [prev.position[0], p])
        end
      else raise('invalid value')
      end
    end

    positions = path.map(&:position)
    duplicated = positions.detect { |p| positions.count(p) > 1 }

    assert_equal 163, duplicated[0] + duplicated[1]
  end
end
