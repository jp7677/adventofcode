# frozen_string_literal: true

require 'minitest/autorun'
require_relative 'util'

class Keypad
  L = 'L'
  R = 'R'
  U = 'U'
  D = 'D'

  @current = ''

  def initialize
    @current = '5'
  end

  def button
    @current
  end

  def move(instruction)
    @current =
      case @current
      when '1'
        case instruction
        when R then '2'
        when D then '4'
        else @current
        end
      when '2'
        case instruction
        when L then '1'
        when R then '3'
        when D then '5'
        else @current
        end
      when '3'
        case instruction
        when L then '2'
        when D then '6'
        else @current
        end
      when '4'
        case instruction
        when R then '5'
        when U then '1'
        when D then '7'
        else @current
        end
      when '5'
        case instruction
        when L then '4'
        when R then '6'
        when U then '2'
        when D then '8'
        else @current
        end
      when '6'
        case instruction
        when L then '5'
        when U then '3'
        when D then '9'
        else @current
        end
      when '7'
        case instruction
        when R then '8'
        when U then '4'
        else @current
        end
      when '8'
        case instruction
        when L then '7'
        when R then '9'
        when U then '5'
        else @current
        end
      when '9'
        case instruction
        when L then '8'
        when U then '6'
        else @current
        end
      else raise('invalid value')
      end
  end

  def move2(instruction)
    @current =
      case @current
      when '1'
        case instruction
        when D then '3'
        else @current
        end
      when '2'
        case instruction
        when R then '3'
        when D then '6'
        else @current
        end
      when '3'
        case instruction
        when L then '2'
        when R then '4'
        when U then '1'
        when D then '7'
        else @current
        end
      when '4'
        case instruction
        when L then '3'
        when D then '8'
        else @current
        end
      when '5'
        case instruction
        when R then '6'
        else @current
        end
      when '6'
        case instruction
        when L then '5'
        when R then '7'
        when U then '2'
        when D then 'A'
        else @current
        end
      when '7'
        case instruction
        when L then '6'
        when R then '8'
        when U then '3'
        when D then 'B'
        else @current
        end
      when '8'
        case instruction
        when L then '7'
        when R then '9'
        when U then '4'
        when D then 'C'
        else @current
        end
      when '9'
        case instruction
        when L then '8'
        else @current
        end
      when 'A'
        case instruction
        when R then 'B'
        when U then '6'
        else @current
        end
      when 'B'
        case instruction
        when L then 'A'
        when R then 'C'
        when U then '7'
        when D then 'D'
        else @current
        end
      when 'C'
        case instruction
        when L then 'B'
        when U then '8'
        else @current
        end
      when 'D'
        case instruction
        when U then 'B'
        else @current
        end
      else raise('invalid value')
      end
  end
end

describe 'day02' do
  it 'solves part 01' do
    input = Util.read_input('day02-input.txt')

    keypad = Keypad.new

    code = input.reduce('') do |acc, it|
      it.chars.each do |c|
        keypad.move(c)
      end
      acc + keypad.button
    end

    assert_equal '69642', code
  end

  it 'solves part 02' do
    input = Util.read_input('day02-input.txt')

    keypad = Keypad.new
    code = input.reduce('') do |acc, it|
      it.chars.each do |c|
        keypad.move2(c)
      end
      acc + keypad.button
    end

    assert_equal '8CB23', code
  end
end
