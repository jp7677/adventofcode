# frozen_string_literal: true

require 'minitest/autorun'
require 'digest'
require_relative 'util'

def yield_hashes
  input = Util.read_input('day05-input.txt').first

  index = 0
  Enumerator.new do |yielder|
    loop do
      index += 1
      hash = Digest::MD5.hexdigest(input + index.to_s)
      yielder << hash if hash.match(/^00000/)
    end
  end
end

describe 'day05' do
  it 'solves part 01' do
    skip('slow ...')

    e = yield_hashes

    hashes = (0..7).to_a.reduce([]) do |acc, _|
      acc + [e.next]
    end

    password = hashes.map do |it|
      it.chars[5]
    end

    assert_equal '2414bc77', password.join
  end

  it 'solves part 02' do
    skip('slow ...')

    e = yield_hashes
    password = {}

    loop do
      hash = e.next
      chr = hash.chars[5]
      next unless chr.match?(/[[:digit:]]/)

      pos = chr.to_i
      password[pos] = hash.chars[6] if (pos < 8) && !password.include?(pos)
      break if password.size == 8
    end

    password = password.sort_by(&:first).map { |it| it[1] }

    assert_equal '437e60fc', password.join
  end
end
