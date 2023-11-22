import { shallowMount } from "@vue/test-utils";
import user from "@/pages/user.vue";

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(user, {
    propsData: {
      name: "test",
    },
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe("user", () => {
  test("is a vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });
});
