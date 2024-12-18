# frozen_string_literal: true

require 'minitest/autorun'
require_relative 'util'

describe 'day00' do
  it 'solves part 01' do
    input = Util.read_input('day00-input.txt')

    assert_equal '0', input.first
  end
end
