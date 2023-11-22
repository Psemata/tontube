import { shallowMount } from "@vue/test-utils";
import register from "@/pages/register.vue";

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(register, {
    propsData: {},
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe("register", () => {
  test("is a vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  test("data", () => {
    expect(wrapper.vm.userData).toStrictEqual({
      username: "",
      email: "",
      password: "",
      password2: "",
    });
  });
});
