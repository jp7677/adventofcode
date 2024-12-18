# frozen_string_literal: true

module Util
  # @param name [String]
  # @return [Array<String>]
  def self.read_input(name)
    File
      .readlines(File.join(__dir__ || raise('no current dir'), "../data/#{name}"))
      .map(&:strip)
  end
end
