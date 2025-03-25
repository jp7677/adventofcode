# frozen_string_literal: true

require 'minitest/autorun'
require_relative 'util'

describe 'day04' do
  it 'solves part 01' do
    input = Util.read_input('day04-input.txt')

    rooms = input.map do |line|
      p = line.split('[')
      [p[0], p[1][0...-1]]
    end

    rooms = rooms.select do |room|
      letters = room[0]
                .chars.reject { |c| '-0123456789'.chars.include?(c) }
                .group_by(&:itself)
                .sort_by { |g| [g[1].size * -1, g[0]] }
                .take(room[1].size)
                .map { |g| g[0] }
                .join
      letters == room[1]
    end

    sector_ids = rooms.map do |room|
      room[0].split('-').last.to_i
    end

    assert_equal 1514, sector_ids.sum
  end

  it 'solves part 02' do
    input = Util.read_input('day04-input.txt')

    rooms = input.map do |line|
      p = line.split('[')
      [p[0], p[1][0...-1]]
    end

    # @param [char] letter
    # @param [int] id
    def rotate_forward(letter, id)
      # ascii a-z -> 97-122
      ((((letter.ord - 96) + id) % (122 - 96)) + 96).chr
    end

    names = rooms.map do |room|
      id = room[0].split('-').last.to_i
      name = room[0].chars.map { |c| c == '-' ? ' ' : rotate_forward(c, id) }.join
      [name, id]
    end

    north_pole = names.select { |name| name[0].match(/^northpole/) }.first

    assert_equal 324, north_pole[1]
  end
end
