import { shallowMount } from "@vue/test-utils";
import Tag from "@/components/Tag.vue";

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(Tag, {
    propsData: {
      name : 'test',
    },
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe("Tag", () => {
  test("is a vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  test("props", () => {
    expect(wrapper.props()).toStrictEqual({
      name: 'test',
    });
  });
});
