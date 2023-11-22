import { shallowMount } from "@vue/test-utils";
import login from "@/pages/login.vue";

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(login, {
    propsData: {},
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe("login", () => {
  test("is a vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  test("data", () => {
    expect(wrapper.vm.userData).toStrictEqual({
      username: "",
      password: "",
    });
  });
});
