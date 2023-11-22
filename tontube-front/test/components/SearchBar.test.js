import { shallowMount } from '@vue/test-utils';
import SearchBar from '@/components/SearchBar.vue';

let wrapper;

beforeEach(() => {
  wrapper = shallowMount(SearchBar, {
    propsData: {},
    mocks: {},
    stubs: {},
    methods: {},
  });
});

afterEach(() => {
  wrapper.destroy();
});

describe('SearchBar', () => {
  test('is a vue instance', () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  test("data", () => {
    expect(wrapper.vm.searchKeyWords).toBe("");
    expect(wrapper.vm.searchedResult).toStrictEqual([]);
  });
});
