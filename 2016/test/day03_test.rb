# frozen_string_literal: true

require 'minitest/autorun'
require_relative 'util'

# @param [Array[int] side
def valid_triangle(side)
  side[0] + side[1] > side[2] && side[0] + side[2] > side[1] && side[1] + side[2] > side[0]
end

describe 'day03' do
  it 'solves part 01' do
    input = Util.read_input('day03-input.txt')

    sides = input.map do |line|
      line.split.map(&:to_i)
    end

    valid = sides.select do |side|
      valid_triangle(side)
    end

    assert_equal 1050, valid.size
  end

  it 'solves part 02' do
    input = Util.read_input('day03-input.txt')

    sides = input.map { |line| line.split.map(&:to_i) }

    sides = sides.map { |s| s[0] } +
            sides.map { |s| s[1] } +
            sides.map { |s| s[2] }

    valid = sides
            .each_slice(3)
            .select { |side| valid_triangle(side) }

    assert_equal 1921, valid.size
  end
end
