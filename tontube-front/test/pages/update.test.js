import { mount } from "@vue/test-utils";
import update from "@/pages/update.vue";

describe("update page", () => {
  test("mount", () => {
    const wrapper = mount(update);
    expect(wrapper.vm).toBeTruthy();
  });
});
