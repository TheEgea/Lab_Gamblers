import { fireEvent, render, screen } from '@testing-library/react';
import TestLink from './TestLink';

describe('Testlink', () => {
  it('shoudl render the component', () => {
    render(<TestLink page="test">Hello</TestLink>);

    fireEvent.mouseEnter(screen.getByText('Hello'));
    fireEvent.mouseLeave(screen.getByText('Hello'));

    expect(screen.getByText('Hello')).toBeInTheDocument();
  });
  it('shoudl render the component', () => {
    render(<TestLink page="">Hello</TestLink>);
  });
});
