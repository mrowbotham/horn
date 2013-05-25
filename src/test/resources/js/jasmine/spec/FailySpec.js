describe("Faily", function() {
  var faily;

  beforeEach(function() {
    faily = new Faily();
  });

  it("should not throw an error (but it does)", function() {
    expect(faily.thrower()).toBeFalsy();
  });
});

describe("Basically bad assertions", function() {
  describe("true", function() {
    it("should be false (but it isn't)", function() {
      expect(true).toBeFalsy();
    });
  });
  describe("1", function() {
    it("should be 2 (but it isn't)", function() {
      expect(1).toEqual(2);
    });
  });
});