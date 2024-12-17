# frozen_string_literal: true

require 'minitest/autorun'

describe 'day00' do
  it 'solves part 01' do
    input =
      File
      .readlines(File.join(__dir__ || raise('no current dir'), '../data/day00-input.txt'))
      .map(&:strip)

    assert_equal '0', input.first
  end
end
