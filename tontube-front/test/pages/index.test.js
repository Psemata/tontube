import { mount } from "@vue/test-utils";
import index from "@/pages/index.vue";

describe("index page", () => {
  test("mount", () => {
    const wrapper = mount(index);
    expect(wrapper.vm).toBeTruthy();
  });
});
